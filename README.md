# rt-key-client

![Java](https://img.shields.io/badge/Java-21%2B-orange)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)

Неофициальный лёгкий Java клиент для API домофонов Ростелеком Ключ.
Подходит для автоматизации, ботов, написания кастомных интерфейсов или интеграции с Home Assistant через MQTT или REST.

Минимум зависимостей - две, JSpecify (api) и Jackson (implementation); HTTP-клиент берётся из JDK. При этом можно передать собственный `HttpClient`.

## Возможности

- **Покрытие API** - аккаунт, устройства, коды, ключи, камеры, медиаконтент камер (скриншоты + live/archive-видеопотоки), события и подписки;
- **Авторизация** — `RtKeyAuth` позволяет авторизоваться по номеру телефона. Также можно использовать свой полученный ранее токен;
- **Без внешнего HTTP-клиента** — встроенный в JDK `java.net.http.HttpClient`;
- **Null-безопасность** — все nullable-значения публичного API помечены @Nullable (JSpecify); соблюдение non-null-контракта проверяет NullAway при сборке;
- **Управление ресурсами и потокобезопасность** — клиент реализует `AutoCloseable` и потокобезопасен;

## Ограничения

Не реализованы `tags`, `banners` и `invoices`.

## Требования

- Java 21+;
- аккаунт Ростелеком «Ключ».

Зависимости подтягиваются автоматически: JSpecify 1.0.1 (compile scope — нужна, если используете `@Nullable` в своём коде) и Jackson 3 (runtime scope). Вручную подключать ничего не нужно.

## Установка

### Gradle

```kotlin
implementation("io.github.anatolii-sennikov:rt-key-client:1.0.0")
```

### Maven

```xml
<dependency>
    <groupId>io.github.anatolii-sennikov</groupId>
    <artifactId>rt-key-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Быстрый старт

### 1. Авторизация и получение токена

```java
import io.github.anatoliisennikov.rtkey.RtKeyAuth;
import io.github.anatoliisennikov.rtkey.model.CodeDispatch;
import io.github.anatoliisennikov.rtkey.model.Session;

import java.util.UUID;

// device id должен быть одинаковым для sendCode и login, иначе HTTP 400 (код 1025, Invalid device_id)
UUID deviceId = UUID.randomUUID();

try (RtKeyAuth auth = RtKeyAuth.builder()
        .deviceId(deviceId)
        .build()) {

    CodeDispatch dispatch = auth.sendCode("79001234567");   // отправляет SMS-код. Телефон БЕЗ +
    String userCode = readCodeFromUser();                      // ваш ввод кода
    Session session = auth.login(dispatch, userCode);

    String accessToken = session.accessToken();
}
```

### 2. Создание клиента

```java
import io.github.anatoliisennikov.rtkey.RtKeyClient;

// Из токена:
RtKeyClient client = RtKeyClient.builder(accessToken).build();

// Либо из поставщика (supplier), чтобы клиент всегда брал свежий токен:
RtKeyClient client = RtKeyClient.builder(() -> tokenStore.currentToken()).build();
```

### 3. Вызов API

```java
try (RtKeyClient client = RtKeyClient.builder(accessToken).build()) {
    var me = client.account().user();
    System.out.println(me);
}
```

## Использование

Клиент предоставляет восемь сервисов, каждый представлен отдельным интерфейсом:

| Метод доступа | Интерфейс | Назначение |
|---------------|-----------|------------|
| `client.account()` | `AccountService` | Текущий пользователь, данные компании / квартиры |
| `client.devices()` | `DeviceService` | Устройства-домофоны, переименование, открытие |
| `client.codes()` | `CodeService` | Коды доступа |
| `client.keys()` | `KeysService` | CRUD RFID-ключей |
| `client.cameras()` | `CameraService` | Список камер, календарь / интервалы архива |
| `client.cameraMedia()` | `CameraMediaService` | Скриншоты и live / archive-потоки |
| `client.events()` | `EventService` | События, счётчики звонков, события устройства |
| `client.subscriptions()` | `SubscriptionService` | Офферы, привилегии, подписки |

### **Аккаунт**

```java
User me = client.account().user();

int flatId = me.flats().stream()
        .findFirst()
        .map(Flat::id)
        .orElseThrow(() -> new IllegalStateException("No flats"));

Company company = client.account().company(flatId);
```

### **Устройства**

```java
Page<Device> devices = client.devices().devices();

int deviceId = devices.items().stream()
        .findFirst()
        .map(Device::id)
        .orElseThrow(() -> new IllegalStateException("No devices"));

Page<Device> devicePage = client.devices().devices(deviceId);   // карточки по идентификатору
List<Intercom> intercoms = client.devices().intercoms();

int intercomId = Integer.parseInt(intercoms.stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No intercoms"))
        .id());

Intercom renamed = client.devices().renameIntercom(intercomId, "Подъезд 1");
client.devices().open(deviceId);   // открыть
```

`findFirst()` в примерах — только для наглядности. Если квартир, устройств или домофонов несколько, выбирайте нужное по `Flat.number()` / адресу, `Device.title()` / `deviceType` / `cameraUuid()`. `Intercom.id()` в API — строка, а `renameIntercom` принимает `int`, поэтому id приводится через `Integer.parseInt`.

### **Коды**

```java
Page<Code> all = client.codes().codes();
Page<Code> temporary = client.codes().codes(CodeType.TEMPORARY);
GeneratedCode generated = client.codes().generateCode(flatId, deviceId);
List<EmergencyCode> emergency = client.codes().emergencyCodes(deviceId);
Intercode intercode = client.codes().intercode(flatId);
client.codes().deleteIntercode(flatId);
```

### **RFID-ключи**

```java
Page<Rfid> all = client.keys().rfids();
Page<Rfid> rfids = client.keys().rfids(
        LimitOffsetQuery.builder().limit(50).offset(0).build());

Key key = client.keys().createKey(flatId, rfidUid, "My key");
client.keys().renameKey(key.id(), "Spare key");
client.keys().deleteRfid(key.id());
```

### **Камеры и медиаконтент**

```java
List<CameraData> cameras = client.cameras().cameras();
List<CameraData> page = client.cameras().cameras(
        LimitOffsetQuery.builder().limit(10).build());

CameraData camera = cameras.stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No cameras"));

// Календарь и интервалы архива
List<LocalDate> days = client.cameras().archiveCalendar(camera.uid());
List<ArchiveInterval> intervals = client.cameras().archiveIntervals(camera.uid());

// Скриншот (null = последний)
byte[] image = client.cameraMedia().screenshot(camera, ScreenshotSize.MEDIUM, null);

// Скриншот на заданный момент
byte[] precise = client.cameraMedia().screenshotPrecise(camera, ScreenshotSize.ORIGINAL, instant);

// Live-поток - закрывайте самостоятельно
try (CameraStream live = client.cameraMedia().live(camera)) {
    InputStream frames = live.body();
    byte[] buffer = new byte[8192];
    int read;
    while ((read = frames.read(buffer)) != -1) {
        // передать buffer[0..read) плееру или записать
    }
}

// Архивный поток - закрывайте так же самостоятельно
try (CameraStream archive = client.cameraMedia().archive(camera, from)) {
    InputStream frames = archive.body();
}
```

### **События**

```java
int bells = client.events().bellCount();

// Звонки с фильтром
int newBells = client.events().bellCount(BellCountQuery.builder()
        .statusTypes(NotificationStatus.NEW)
        .build());

List<Event> events = client.events().events(from, to);

// События с фильтром по типам
List<Event> opened = client.events().events(from, to, EventsQuery.builder()
        .limit(100)
        .eventTypes(EventTypes.CODE_OPEN_LOCAL, EventTypes.RFID_OPEN_LOCAL)
        .build());

int total = client.events().eventsAmount(from, to);
List<DeviceEvent> deviceEvents = client.events().deviceEvent(deviceId);
```

### **Подписки**

```java
List<Offer> offers = client.subscriptions().offers();
List<Offer> companyOffers = client.subscriptions().offers(OfferQuery.builder()
        .types(OfferType.COMPANY)
        .build());
List<Privilege> privileges = client.subscriptions().privileges();
List<Subscription> subs = client.subscriptions().subscriptions();
```

## Конфигурация

Опции `RtKeyClient.Builder` / `RtKeyAuth.Builder`:

| Метод | По умолчанию | Описание |
|-------|--------------|----------|
| `connectTimeout(Duration)` | `5с`, если клиент создаёт `HttpClient` | Таймаут TCP-соединения. **Нельзя** сочетать с `httpClient(...)`. |
| `requestTimeout(Duration)` | `20с` | Таймаут получения ответа. |
| `httpClient(HttpClient)` | внутренний клиент | Передать собственный настроенный JDK `HttpClient`. Клиент **не закрывает** переданный экземпляр. |
| `baseUri(Host, URI)` | production-хосты | Переопределить origin хоста (staging / тесты). В `RtKeyClient` — любой `Host`, в `RtKeyAuth` — только `Host.KEYAPIS`. |

`requestTimeout` — таймаут получения ответа; для `CameraStream` он не ограничивает чтение тела потока.

`baseUri` принимает только origin: `http(s)://host[:port]`, без path, query и fragment. Например, `http://localhost:8080`; значение `http://localhost:8080/api` вызовет `IllegalArgumentException`.

Production-хосты:

| `Host` | Базовый URI | Что обслуживает |
|--------|-------------|-----------------|
| `HOUSEHOLD` | `https://household.key.rt.ru` | Аккаунт, коды, устройства, RFID-ключи, события устройства |
| `KEYAPIS` | `https://keyapis.key.rt.ru` | Авторизация (SMS-код и вход), список камер, счётчик звонков, офферы и подписки |
| `VC` | `https://vc.key.rt.ru` | Календарь и интервалы архива камер |
| `EVENTS` | `https://events.key.rt.ru` | Лента событий и их количество |

Переопределения в `RtKeyClient` и в `RtKeyAuth` независимы: `baseUri(Host.KEYAPIS, ...)` у клиента не влияет на авторизацию — у `RtKeyAuth` свой builder.

### Токен и повторные попытки

`Supplier<String>` переданный в `RtKeyClient.builder(Supplier)` вызывается перед каждым запросом. Он должен быть потокобезопасным и возвращать непустой токен: `null` или пустая строка приводят к `IllegalStateException`.

Клиент не обновляет токен и не выполняет автоматических retry. Обновление токена, повторные попытки и обработка `401` — ответственность вызывающего.

## Обработка ошибок

Ошибки выполнения запросов — непроверяемые (unchecked) и наследуются от `RtKeyException`:

`getRequestId()` объявлен в базовом `RtKeyException`, но у `RtKeyNetworkException` и `RtKeyContractException` всегда возвращает `null`; серверный request id доступен только в `RtKeyApiException`.

Нарушения предусловий обнаруживаются до отправки запроса и приводят к другим unchecked-исключениям:

- `NullPointerException` — обязательный аргумент равен `null` (включая отсутствующий `deviceId` в `RtKeyAuth.Builder.build()`);
- `IllegalArgumentException` — неположительный id, `limit <= 0`, отрицательный `offset`, неположительный `Duration`, невалидный `baseUri`;
- `IllegalStateException` — клиент закрыт, `tokenSupplier` вернул `null` или пустую строку, а также одновременная передача `connectTimeout` и `httpClient` (проверяется в `build()`).

```java
try {
    client.devices().open(deviceId);
} catch (RtKeyApiException e) {
    log.warn("API error {} ({}): {}", e.getStatus(), e.getServerCode(), e.getDescription());
} catch (RtKeyNetworkException e) {
    // сеть/таймаут — можно повторить вручную
} catch (RtKeyContractException e) {
    // API изменился или баг маппинга — стоит сообщить
}
```

## Управление ресурсами и потокобезопасность

- `RtKeyClient`, `RtKeyAuth` и `CameraStream` реализуют `AutoCloseable` —
  используйте try-with-resources или вызывайте `close()` по завершении.
- `RtKeyClient` не хранит изменяемого состояния на запрос и использует внутри HttpClient из JDK
  `HttpClient`; создайте **один** экземпляр и используйте его между потоками,
  а не создавайте новый клиент на каждый вызов.
- Если вы передали свой `HttpClient` через `httpClient(...)`, клиент его **не закрывает** — управляйте его жизненным циклом самостоятельно. Внутренний `HttpClient`,  созданный SDK, закрывается вместе с `RtKeyClient`/`RtKeyAuth`.
- Всегда закрывайте `CameraStream`, чтобы освободить соединение. `close()` может выбросить `RtKeyNetworkException`, если не удалось закрыть тело ответа.

## Структура проекта

```text
io.github.anatoliisennikov.rtkey
├── RtKeyClient, RtKeyAuth, Host     # точки входа и хосты API
├── *Service                         # интерфейсы публичного API
├── CameraStream                     # публичный класс медиапотока
├── model/                           # публичные типизированные модели (records, enum, запросы)
├── exception/                       # публичная иерархия исключений
└── internal/                        # транспорт, кодеки, DTO
```

Публичным API считаются только классы вне пакета `internal/`.

## Версионирование и совместимость

Проект следует [Semantic Versioning](https://semver.org/). В рамках `1.x`
публичный API бинарно совместим; breaking changes выпускаются в `2.0.0`.

## Сборка из исходников

```bash
./gradlew build     # полная сборка, тесты и статический анализ
./gradlew test      # запуск тестов
```

## Вклад в проект

Приветствуются issues и pull requests. Сначала откройте issue для обсуждения
существенных изменений и добавляйте тесты на новое поведение.

## Лицензия

Распространяется на условиях [Apache License 2.0](LICENSE).

## Дисклеймер

Это **неофициальный** клиент. Он не связан ни с Ростелекомом, ни с командой Ростелеком Ключ,
не одобрен и не спонсируется ими. Лежащий в основе API является проприетарным
и может измениться без предупреждения. Вы несёте ответственность за соблюдение
условий обслуживания сервиса и за безопасное хранение своих учётных данных
и токенов.