# apache-camel-demo

This is a simpel demo of Apache Camel.

## Example camel flow ?
![alt tag](https://github.com/vachacz/apache-camel-demo/blob/master/diagram.png?raw=true)

## How to use ?

After executing the main method in CamelDemo class put **input/text.txt** file will be processed by the example
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
three files will be produced

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
