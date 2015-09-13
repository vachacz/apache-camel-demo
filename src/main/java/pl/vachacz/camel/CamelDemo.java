package pl.vachacz.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class CamelDemo {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://input")
                    .split(body(String.class).tokenize("\n"))
                    .transform(body().regexReplaceAll("\\r", ""))
                    .choice()
                        .when(body().startsWith("[ORDER]"))
                            .setHeader("type", constant(1))
                            .to("direct:order")
                        .when(body().startsWith("[COMPLAINT]"))
                            .setHeader("type", constant(2))
                            .to("direct:complaint")
                        .otherwise()
                            .setHeader("type", constant(3))
                            .to("direct:return")
                    .end();

                from("direct:order")
                        .transform(body().append(" => TRANSFORMED"))
                    .multicast().to("direct:agg", "stream:out");

                from("direct:complaint")
                    .filter(body().not().contains("FAKE"))
                    .multicast().to("direct:agg", "stream:out");

                from("direct:return")
                    .multicast().to("direct:agg", "stream:out");

                from("direct:agg")
                    .aggregate(header("type"), new Aggregator()).completionSize(2).completionTimeout(1000)
                        .bean(OrderService.class, "doSomething")
                        .setHeader(Exchange.FILE_NAME, simple("type_${header.type}.txt"))
                        .transform(body().append("\n"))
                        .to("file:out?fileExist=Append");
            }
        });

        context.start();

        Thread.sleep(1000000);

        context.stop();
    }

    public static class OrderService {
        public void doSomething(Exchange exchange) {
            System.out.println("---> " + exchange.getIn().getBody().toString());
        }
    }

    static class Aggregator implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange;
            }
            oldExchange.getIn().setBody(newExchange.getIn().getBody() + " + " + newExchange.getIn().getBody());
            return oldExchange;
        }
    }

}
