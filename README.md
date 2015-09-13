# apache-camel-demo

This is a simple demo of Apache Camel.

## How to use ?

After executing the main method in CamelDemo class, **input/text.txt** file will be processed. As soon as the file is processed, its moved to **.camel** folder, but the code still polls for new files, so you can add more files to the **input** folder.

Logic of the example is very simple. All records are printed on stdout. Additionally the code appends aggregated orders/complaints/returns to a approperiate type_<type_id>.txt.

![alt tag](https://github.com/vachacz/apache-camel-demo/blob/master/diagram.png?raw=true)

Read more:
http://camel.apache.org/enterprise-integration-patterns.html

```
[ORDER] 1234 4
[COMPLAINT] 5423 2
[ORDER] 1223 1
[RETURN] 3256 1
[COMPLAINT] 456 5 FAKE
[COMPLAINT] 134234 2 FAKE
[COMPLAINT] 5444 2
[ORDER] 1223 1
[ORDER] 1223 4
```
Three files will be produced

**type_1.txt**
```
[ORDER] 1223 1 => TRANSFORMED + [ORDER] 1223 1 => TRANSFORMED
[ORDER] 1223 4 => TRANSFORMED + [ORDER] 1223 4 => TRANSFORMED
```

**type_2.txt**
```
[COMPLAINT] 5444 2 + [COMPLAINT] 5444 2
```

**type_3.txt**
```
[RETURN] 3256 1
```
