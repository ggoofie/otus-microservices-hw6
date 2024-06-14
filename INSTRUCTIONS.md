### Инструкции

- решение представляет собой _Api Gateway_, 
написанный с помощью _Spring Cloud Gateway_, 
на котором реализрована _JWT_-аутентификация 

<br>

![Architecture scheme.](otus-microservices-hw6.svg)

<br>

- создаем _namespace_ и устанавливаем приложение из _Helm_
```console
kubectl create namespace <namespace>
helm upgrade --install -n <namespace> <release> ./gateway-chart
``` 
<br>

- запускаем тесты _Postman_ с помощью _newman_
```console
newman run ./postman-test/otus_microservices_hw6.postman_collection.json --verbose
```
<br>

- [результат](postman-test/newman_output) запуска тестов с помощью _newman_ 
