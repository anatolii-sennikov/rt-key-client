package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Subscription;
import io.github.anatoliisennikov.rtkey.model.SubscriptionQuery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class SubscriptionsOperationTest extends BaseOperationTest {
    @Test
    public void subscriptions() throws IOException {
        final String data = getResourceAsString("/subscription/subscriptions.json");

        when(transport.get(eq(Host.KEYAPIS), eq("/subscription/api/v1/subscription/list"), eq(Query.empty()), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Subscription> subscriptions = SubscriptionsOperation.call(transport, tokenSupplier, SubscriptionQuery.empty());

        assertThat(subscriptions).usingRecursiveComparison().isEqualTo(expectedSubscriptions());
    }

    @Test
    public void subscriptionsWithQuery() throws IOException {
        final String data = getResourceAsString("/subscription/subscriptions.json");

        SubscriptionQuery query = SubscriptionQuery.builder()
                .limit(10)
                .flatNumbers("136", "137")
                .orpons("113156294")
                .build();

        Query expectedQuery = Query.empty()
                .add("paging.limit", "10")
                .add("filter.flatNumbers", "136")
                .add("filter.flatNumbers", "137")
                .add("filter.orpons", "113156294");

        when(transport.get(eq(Host.KEYAPIS), eq("/subscription/api/v1/subscription/list"), eq(expectedQuery), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Subscription> subscriptions = SubscriptionsOperation.call(transport, tokenSupplier, query);

        assertThat(subscriptions).usingRecursiveComparison().isEqualTo(expectedSubscriptions());
    }

    private static List<Subscription> expectedSubscriptions() {
        return List.of(
                new Subscription(
                        "ffffea51-0c7a-4895-aa64-97c11bae549f", "76296410", "226", 37721,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2026-09-14T08:38:42Z"), null, true,
                        "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                37721, "RTK", "76296410", 83, 30, true,
                                "ACTIVE", null, false, "2026-04-02T16:06:53Z", null,
                                OfferFixtures.expectedPlan(
                                        30, "Минимальное мобильное приложение Домофона (B2C АСР МРФ)",
                                        "Добавить/удалить ключ\nОткрыть дверь по кнопке в МП",
                                        "2024-01-13T10:40:02Z", "2025-10-30T11:59:16.489998Z", null,
                                        List.of("INTERCOM_RFID_USE", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE", "INTERCOM_USE", "INTERCOM_OPEN")
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffffdd39-e5e2-4aa2-8988-ebeaa70eb03b", "53130607", "83", 3594,
                        "CANCELED", "ACTIVE", 50.0, Instant.parse("2023-03-10T01:28:35Z"),
                        Instant.parse("2023-03-10T01:33:32.794494Z"), false, "ASR_MRF",
                        null, null, "423000917191",
                        OfferFixtures.expectedOffer(
                                3594, "RTK", "53130607", 75, 5, false,
                                "BLOCKED", 50.0, true, "2022-03-24T07:02:38Z",
                                "2025-11-12T08:20:20.007875Z",
                                OfferFixtures.expectedPlan(
                                        5, "Умный домофон",
                                        "Получение видеозвонка с домофона на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nВременные коды для гостей и курьеров\nИстория событий",
                                        "2021-03-18T08:17:08Z", "2023-06-06T08:48:15.081316Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffffd0cb-d091-43dd-900f-0d8b351102e7", "107301082", "683", 16782,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2026-05-13T06:29:29Z"), null, true,
                        "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                16782, "RTK", "107301082", 280, 4,
                                true, "ACTIVE", null, false,
                                "2024-04-27T08:41:11Z", null,
                                OfferFixtures.expectedPlan(
                                        4, "Минимальное мобильное приложение Домофона",
                                        "Добавить/удалить ключ\nОткрыть дверь по кнопке в МП",
                                        "2021-03-18T08:12:02Z", "2025-10-30T11:56:27.853925Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffffce2a-4e3d-4cc6-8e1c-26a323363173", "113220548", "21", 18797,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2024-11-27T07:15:31Z"), null, true,
                        "ASR_MC", null, null, null,
                        OfferFixtures.expectedOffer(
                                18797, "COMPANY", "113220548", 1632, 16,
                                true, "ACTIVE", null, false,
                                "2024-08-28T12:35:04Z", null,
                                OfferFixtures.expectedPlan(
                                        16, "Умный шлагбаум", null,
                                        "2021-03-18T16:57:40Z", "2022-11-16T09:41:59.442349Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffffcb5b-20bd-4c57-b13b-e973478a5ac5", "27343503", "70", 2404,
                        "CANCELED", "ACTIVE", 50.0, Instant.parse("2023-03-10T06:14:40Z"),
                        Instant.parse("2023-03-10T06:25:40.875224Z"), false, "ASR_MRF",
                        null, null, "423000917562",
                        OfferFixtures.expectedOffer(
                                2404, "RTK", "27343503", 75, 5, false,
                                "BLOCKED", 50.0, true,
                                "2022-02-11T05:31:46Z", "2025-11-12T08:17:21.626754Z",
                                OfferFixtures.expectedPlan(
                                        5, "Умный домофон",
                                        "Получение видеозвонка с домофона на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nВременные коды для гостей и курьеров\nИстория событий",
                                        "2021-03-18T08:17:08Z", "2023-06-06T08:48:15.081316Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffffc330-8fd2-4ee3-b7af-b4ff021b720c", "26176507", "37", 913, "ACTIVE",
                        "STATUS_TYPE_UNKNOWN", 50.0, Instant.parse("2025-10-01T10:46:42Z"),
                        Instant.parse("2026-02-09T10:40:37.998890Z"), false, "ASR_MRF",
                        null, null, "363020779813",
                        OfferFixtures.expectedOffer(
                                913, "RTK", "26176507", 502, 5, false,
                                "ACTIVE", 50.0, false,
                                "2021-10-06T11:41:42Z", null,
                                OfferFixtures.expectedPlan(
                                        5, "Умный домофон",
                                        "Получение видеозвонка с домофона на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nВременные коды для гостей и курьеров\nИстория событий",
                                        "2021-03-18T08:17:08Z", "2023-06-06T08:48:15.081316Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffffbe99-69a9-4a20-a07f-1236bf41f811", "14098002", "36", 16426,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null, Instant.parse("2024-04-08T11:59:17Z"),
                        null, true, "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                16426, "RTK", "14098002", 1510, 8, true,
                                "ACTIVE", null, false,
                                "2024-04-08T11:59:16Z", null,
                                OfferFixtures.expectedPlan(
                                        8, "Трубка", null,
                                        "2021-03-18T08:28:29Z", "2022-11-16T09:53:52.106840Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff9edf-a339-4d50-af6f-7fcb3df775a8", "106792676", "105", 5101,
                        "CANCELED", "ACTIVE", 80.0, Instant.parse("2022-11-30T09:01:00Z"),
                        Instant.parse("2026-04-27T04:00:04.138710Z"), false, "ASR_MRF",
                        null, null, "566013477616",
                        OfferFixtures.expectedOffer(
                                5101, "RTK", "106792676", 102, 13, false,
                                "BLOCKED", 80.0, true,
                                "2022-04-15T09:20:59Z", "2025-10-23T12:19:09.800717Z",
                                OfferFixtures.expectedPlan(
                                        13, "Умный домофон + обслуживание + трубка",
                                        "Обслуживание домофона\nВидеозвонок на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nПолучение звонка на домофонную трубку. Стоимость трубки и её установки оплачивается отдельно. Для подключения: 8 800 301 05 50",
                                        "2021-03-18T16:53:51Z", "2022-11-16T09:58:02.522866Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff8d60-0165-4035-93be-3cf68f12d684", "106793477", "114", 13461,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2026-01-23T08:26:06Z"), null, true,
                        "ASR_MC", null, null, null,
                        OfferFixtures.expectedOffer(
                                13461, "COMPANY", "106793477", 1999, 17, true,
                                "ACTIVE", null, false,
                                "2023-06-28T06:58:09Z", "2024-10-16T12:57:20.951234Z",
                                OfferFixtures.expectedPlan(
                                        17, "Видеонаблюдение. Мобильное приложение+Архив", null,
                                        "2021-03-18T16:58:48Z", "2024-06-06T09:35:39.878440Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff8ada-b52f-4852-919b-4a77c5c7ee97", "36520044", "216", 1400,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null, Instant.parse("2023-07-28T10:24:03Z"),
                        null, true, "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                1400, "RTK", "36520044", 102, 4, true,
                                "BLOCKED", null, false,
                                "2021-11-09T05:47:38Z", "2025-10-23T07:47:35.647894Z",
                                OfferFixtures.expectedPlan(
                                        4, "Минимальное мобильное приложение Домофона",
                                        "Добавить/удалить ключ\nОткрыть дверь по кнопке в МП",
                                        "2021-03-18T08:12:02Z", "2025-10-30T11:56:27.853925Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff8783-eb73-4c9b-9586-0f65baeeb306", "106864891", "115", 11008,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null, Instant.parse("2024-06-23T14:06:04Z"),
                        Instant.parse("2024-07-31T12:28:36.730926Z"), true, "ASR_MC",
                        null, null, null,
                        OfferFixtures.expectedOffer(
                                11008, "COMPANY", "106864891", 755, 16,
                                true, "ACTIVE", null, false,
                                "2023-01-17T10:26:55Z", "2024-12-02T12:10:14.958069Z",
                                OfferFixtures.expectedPlan(
                                        16, "Умный шлагбаум", null,
                                        "2021-03-18T16:57:40Z", "2022-11-16T09:41:59.442349Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff8194-f014-4c35-9891-a9c01cb21398", "103466576", "135", 20972,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN",
                        null, Instant.parse("2025-01-09T08:23:52Z"), null, true,
                        "ASR_MC", null, null, null,
                        OfferFixtures.expectedOffer(
                                20972, "COMPANY", "103466576", 1254, 16,
                                true, "ACTIVE", null, false,
                                "2025-01-09T08:23:42Z", "2025-02-25T06:46:50.410300Z",
                                OfferFixtures.expectedPlan(
                                        16, "Умный шлагбаум", null,
                                        "2021-03-18T16:57:40Z", "2022-11-16T09:41:59.442349Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff756c-2f92-4247-b57f-1d7a3422e969", "48208351", "4121", 42662,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN",
                        null, Instant.parse("2026-10-09T05:45:56Z"), null, true,
                        "ASR_MC", null, null, null,
                        OfferFixtures.expectedOffer(
                                42662, "COMPANY", "48208351", 1057, 8, true,
                                "ACTIVE", null, false,
                                "2026-10-09T05:45:50Z", null,
                                OfferFixtures.expectedPlan(
                                        8, "Трубка", null,
                                        "2021-03-18T08:28:29Z", "2022-11-16T09:53:52.106840Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff5447-ecb2-46fb-8351-e69b1e081280", "102453778", "238", 6527,
                        "CANCELED", "ACTIVE", null, Instant.parse("2023-11-20T05:45:41Z"),
                        Instant.parse("2024-07-29T16:13:08.026528Z"), false, "ASR_MRF",
                        null, null, "356020243431",
                        OfferFixtures.expectedOffer(
                                6527, "RTK", "102453778", 376, 13, false, "ACTIVE", null, false,
                                "2022-06-02T06:50:33Z", "2024-07-04T09:37:00.125048Z",
                                OfferFixtures.expectedPlan(
                                        13, "Умный домофон + обслуживание + трубка",
                                        "Обслуживание домофона\nВидеозвонок на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nПолучение звонка на домофонную трубку. Стоимость трубки и её установки оплачивается отдельно. Для подключения: 8 800 301 05 50",
                                        "2021-03-18T16:53:51Z", "2022-11-16T09:58:02.522866Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff4ce9-9de7-4eda-8998-8bccf7bfd9c7", "25935022", "29", 10178,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2023-12-14T13:28:44Z"), null, true,
                        "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                10178, "RTK", "25935022", 235, 17, true,
                                "ACTIVE", null, false, "2022-11-28T17:44:00Z",
                                "2023-12-14T13:28:43.517166Z",
                                OfferFixtures.expectedPlan(
                                        17, "Видеонаблюдение. Мобильное приложение+Архив", null,
                                        "2021-03-18T16:58:48Z", "2024-06-06T09:35:39.878440Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff43ed-1f59-44a2-a453-92af1f82fb59", "107223292", "49", 13993,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2023-12-17T04:46:18Z"), null, true,
                        "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                13993, "RTK", "107223292", 1598, 4,
                                true, "BLOCKED", null, false,
                                "2023-08-31T05:32:16Z", "2025-10-23T12:21:49.494866Z",
                                OfferFixtures.expectedPlan(
                                        4, "Минимальное мобильное приложение Домофона",
                                        "Добавить/удалить ключ\nОткрыть дверь по кнопке в МП",
                                        "2021-03-18T08:12:02Z", "2025-10-30T11:56:27.853925Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff379c-95da-41ef-82b6-b23e47c6d929", "113195999", "81", 37762,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null, Instant.parse("2026-04-06T10:30:52Z"),
                        null, true, "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                37762, "RTK", "113195999", 2456, 4,
                                true, "ACTIVE", null, false,
                                "2026-04-06T10:30:46Z", null,
                                OfferFixtures.expectedPlan(
                                        4, "Минимальное мобильное приложение Домофона",
                                        "Добавить/удалить ключ\nОткрыть дверь по кнопке в МП",
                                        "2021-03-18T08:12:02Z", "2025-10-30T11:56:27.853925Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff22a5-a5bf-456f-b1a9-6311aa417a44", "25294792", "110", 17627,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2024-11-25T12:08:59Z"), null, true,
                        "ASR_MC", null, null, null,
                        OfferFixtures.expectedOffer(
                                17627, "COMPANY", "25294792", 1847, 5, true,
                                "ACTIVE", null, true,
                                "2024-06-10T13:13:27Z", "2025-01-27T07:45:57.752473Z",
                                OfferFixtures.expectedPlan(
                                        5, "Умный домофон",
                                        "Получение видеозвонка с домофона на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nВременные коды для гостей и курьеров\nИстория событий",
                                        "2021-03-18T08:17:08Z", "2023-06-06T08:48:15.081316Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff09db-1f0f-4715-80be-3dec41688d96", "107227082", "44", 19018,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN", null,
                        Instant.parse("2024-11-12T02:34:49Z"), null, true,
                        "ASR_MRF", null, null, null,
                        OfferFixtures.expectedOffer(
                                19018, "RTK", "107227082", 628, 90, true,
                                "ACTIVE", null, false, "2024-09-13T01:23:22Z",
                                "2024-12-02T09:45:01.854077Z",
                                OfferFixtures.expectedPlan(
                                        90, "Минимальное мобильное приложение Домофона (B2C АСР Ключ)",
                                        "Добавить/удалить ключ",
                                        "2023-11-10T13:49:03Z", "2025-10-30T11:59:59.422640Z", null,
                                        List.of()
                                )
                        ),
                        null
                ),
                new Subscription(
                        "ffff02fd-0c52-454d-93d6-f22c489f8927", "111813267", "254", 16801,
                        "ACTIVE", "STATUS_TYPE_UNKNOWN",
                        null, Instant.parse("2024-04-27T11:43:30Z"), null, true,
                        "ASR_MC", null, null, null,
                        OfferFixtures.expectedOffer(
                                16801, "COMPANY", "111813267", 843, 5, true,
                                "ACTIVE", null, false,
                                "2024-04-27T11:43:24Z", "2024-12-02T09:16:48.171256Z",
                                OfferFixtures.expectedPlan(
                                        5, "Умный домофон",
                                        "Получение видеозвонка с домофона на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nВременные коды для гостей и курьеров\nИстория событий",
                                        "2021-03-18T08:17:08Z", "2023-06-06T08:48:15.081316Z", null,
                                        List.of()
                                )
                        ),
                        null
                )
        );
    }
}
