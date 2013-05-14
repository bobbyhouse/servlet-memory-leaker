## Memory Leaker

This is a JavaServlet(tomcat) that will create memory leaks on every POST
request by interning the message in the JSON data that is passed to it.

```
POST /memory-leaker/MemoryLeaker
input: {message: String}
output: {max_memory: Integer, total_memory: Integer, free_memory: Integer}
```

```
GET /memory-leaker/MemoryLeaker
output: {message: String}
```


####Example:

```
>$ curl -X POST http://localhost:8080/memory-leaker/MemoryLeaker -d '{message: a long message}'
{"max_memory":129957888,"total_memory":85000192,"free_memory":72693128}%
```
