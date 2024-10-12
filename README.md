# Базовая настройка

## Запуск minikube

[Инструкция по установке](https://minikube.sigs.k8s.io/docs/start/)

```bash
minikube start
```


## Добавление токена авторизации GitHub

[Получение токена](https://github.com/settings/tokens/new)

```bash
kubectl create secret docker-registry ghcr --docker-server=https://ghcr.io --docker-username=<github_username> --docker-password=<github_token> -n default
```


## Установка API GW kusk

[Install Kusk CLI](https://docs.kusk.io/getting-started/install-kusk-cli)

```bash
kusk cluster install
```


## Настройка terraform

[Установите Terraform](https://yandex.cloud/ru/docs/tutorials/infrastructure-management/terraform-quickstart#install-terraform)


Создайте файл ~/.terraformrc

```hcl
provider_installation {
  network_mirror {
    url = "https://terraform-mirror.yandexcloud.net/"
    include = ["registry.terraform.io/*/*"]
  }
  direct {
    exclude = ["registry.terraform.io/*/*"]
  }
}
```

## Применяем terraform конфигурацию 

```bash
cd terraform
terraform apply
```

## Настройка API GW

```bash
kusk deploy -i api.yaml
```

## Проверяем работоспособность

```bash
kubectl port-forward svc/kusk-gateway-envoy-fleet -n kusk-system 8080:80
curl localhost:8080/hello
```


## Delete minikube

```bash
minikube delete
```
---
# Задание 1

## Задание 1.1

### Функциональность монолитного приложения:
* влючение/выключение отопления
* выставление желаемой температуры дома
* получение температуры с датчиков
* просматривать температуру с датчиков

### Архитектура приложения:
* **Язык программирования:** Java
* **База данных:** PostgreSQL
* **Архитектура:** Монолитная, все компоненты системы (обработка запросов, бизнес-логика, работа с данными) находятся в рамках одного приложения.
* **Взаимодействие:** Синхронное, запросы обрабатываются последовательно.
* **Масштабируемость:** Ограничена, так как монолит сложно масштабировать по частям.
* **Развертывание:** Требует остановки всего приложения.

## Домены монолитного приложения:
* Управление пользователями и домами 
* Управление нагревательными устройствами
* Мониторинг датчиков

## System Context diagram монолитного приложения
[Диаграмма контекста](docs/old/C4_Context.puml)

## Задание 1.2

В новой системе можно выделить следующие микросервисы:
* управление пользователями и домами
* управление устройствами
* микросервис взаимодействия с устройствами отопления
* микросервис взаимодействия с устройствами автоматических ворот
* микросервис взаимодействия с устройствами управления светом
* микросервис взаимодействия с камерами
* микросервис сбора телеметрии
* API гейтвей
* очередь обмена сообщениями (kafka)

Взаимодейтвие межде сервисами в основном сингронное использую REST API. Также микросервисы обмениваются сообщениями через очередь.

[Диаграмма контейнеров](docs/new/C4_Container.puml)

[Диаграмма компонента 1](docs/new/C4_Component_device_service.puml)

[Диаграмма компонента 2](docs/new/C4_Component_telemetry_service.puml)

[Диаграмма компонента 3](docs/new/C4_Component_heating_gateway_service.puml)

[Диаграмма кода (сущности)](docs/new/ER.puml)

## Задание 1.3

[ER Диаграмма](docs/new/ER.puml)

## Задание 1.4

[Open API для сервиса управления устройствами](docs/new/OpenApi_device_service.yaml)

[Open API для сервиса телеметрии](docs/new/OpenApi_telemetry.yaml)

# Задание 2

## Задание 2.1

Созданы для новых сервиса: device-service и telemetry-service.
В микросервисе device-service не определены сущности "User" и "Home" потому что они предполагаются в другом микросервисе. Сущность DeviceType упрощена.

Чтобы собрать и запустить их в k8s нужно выполнить следующие шаги:

Собрать образ для device-service:

```bash
cd device-service
docker build -t device-service .
```
Добавить собранный имедж в minikube:

```bash
minikube image load device-service:latest
```

Установить dependency для helm chart:
```bash
cd charts/device-service
helm dependency build
```

Установить helm chart:
```bash
cd charts/device-service
helm install device-service .
```

С помощью ```kubectl get pods``` проверить что pod *device-service-postgresql-0* в состоянии Ready, до этого момента под *device-service-\<id\>* будет падать с ошибкой и перезапускаться, как только postgresql станет доступной он удачно стартанёт.

Собрать образ для telemetry-service:

```bash
cd telemetry-service
docker build -t telemetry-service .
```

Добавить собранный имедж в minikube:

```bash
minikube image load telemetry-service:latest
```

Установить dependency для helm chart:
```bash
cd charts/telemetry-service
helm dependency build
```

Установить helm chart:
```bash
cd charts/telemetry-service
helm install telemetry-service .
```

С помощью ```kubectl get pods``` проверить что pod *telemetry-service-postgresql-0* в состоянии Ready, до этого момента под *telemetry-service-\<id\>* будет падать с ошибкой и перезапускаться, как только postgresql станет доступной он удачно стартанёт.

## Задание 2.2

### Настройка

Установить [Kusk CLI](https://docs.kusk.io/getting-started/install-kusk-cli)

Выполнить:
```bash
kusk cluster install
```

Настройка Kusk для telemetry-service:

```bash
kusk deploy -i docs/new/OpenApi_telemetry.yaml
```

Настройка Kusk для device-service:

```bash
kusk deploy -i docs/new/OpenApi_device_service.yaml
```

### Проверка

Пробросить порт:

```bash
kubectl port-forward svc/kusk-gateway-envoy-fleet -n kusk-system 8080:80
```
Регистрация нового устройства:

```bash
curl -v --request POST \
  --url http://localhost:8080/devices/register \
  --header 'Content-Type: application/json' \
  --data '{
  "moduleUuid": "testmoduleuuid",
	"houseUuid": "houseuuid",
  "serialNumber": "123456",
  "name": "Device 1",
  "status": "ON",
  "url": "https://someurl"
}'
```

В ответе от серевера будет uuid устройства. Его нужно использовать далее в запросах.

Можно выполнить:
```bash
DEVICE_UUID=<uuid из ответа>
```


Получение информации об устройстве:

```bash
curl -v --request GET \
  --url http://localhost:8080/devices/${DEVICE_UUID}
```


Изменение информации об устройстве:

```bash
curl -v --request PUT \
  --url http://localhost:8080/devices/${DEVICE_UUID} \
  --header 'Content-Type: application/json' \
  --data '{
  "moduleUuid": "testmoduleuuid",
  "serialNumber": "1234567",
  "name": "Device 2",
  "status": "ON",
  "url": "https://someurl"
}'
```

Выключение устройства:

```bash
curl -v --request PUT \
  --url http://localhost:8080/devices/${DEVICE_UUID}/status \
  --header 'Content-Type: application/json' \
  --data '{
  "status": "OFF"
}'
```

Отправление комманды на устройство:

```bash
curl -v --request POST \
  --url http://localhost:8080/devices/${DEVICE_UUID}/commands \
  --header 'Content-Type: application/json' \
  --data '{
  "command": "SET_TEMP",
	"argument": "22"
}'
```

Добавление телеметрии:

```bash
curl -v --request POST \
  --url http://localhost:8080/telemetry \
  --header 'Content-Type: application/json' \
  --data '{
	"deviceUuid": "deviceUUID",
	"houseUuid": "houseUUID",
	"metricName": "temperature",
	"metricValue": "22",
	"timestamp": "2024-10-01T00:11:22"
}'
```

Получение последней телеметрии:

```bash
curl -v --request GET \
  --url http://localhost:8080/devices/deviceUUID/telemetry/latest
```

Получение истории телеметрий:

```bash
curl -v --request GET \
  --url 'http://localhost:8080/devices/deviceUUID/telemetry?from=2024-01-01T00%3A00%3A00&to=2024-12-01T00%3A00%3A00'
```









