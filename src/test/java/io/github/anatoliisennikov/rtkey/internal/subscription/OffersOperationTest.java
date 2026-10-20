package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Offer;
import io.github.anatoliisennikov.rtkey.model.OfferQuery;
import io.github.anatoliisennikov.rtkey.model.OfferType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class OffersOperationTest extends BaseOperationTest {
    @Test
    public void offers() throws IOException {
        final String data = getResourceAsString("/subscription/offers.json");

        when(transport.get(eq(Host.KEYAPIS), eq("/subscription/api/v1/offer/list"), eq(Query.empty()), same(tokenSupplier)))
                .thenReturn(new Response(200, data));
        List<Offer> offers = OffersOperation.call(transport, tokenSupplier, OfferQuery.empty());

        assertThat(offers).usingRecursiveComparison().isEqualTo(expectedOffers());
    }

    @Test
    public void offersWithQuery() throws IOException {
        final String data = getResourceAsString("/subscription/offers.json");

        OfferQuery query = OfferQuery.builder()
                .limit(10)
                .orpons("113156294")
                .types(OfferType.SHARED, OfferType.RTK)
                .build();

        Query expectedQuery = Query.empty()
                .add("paging.limit", "10")
                .add("filter.orpons", "113156294")
                .add("filter.types", "SHARED")
                .add("filter.types", "RTK");

        when(transport.get(eq(Host.KEYAPIS), eq("/subscription/api/v1/offer/list"), eq(expectedQuery), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Offer> offers = OffersOperation.call(transport, tokenSupplier, query);

        assertThat(offers).usingRecursiveComparison().isEqualTo(expectedOffers());
    }

    private static List<Offer> expectedOffers() {
        return List.of(
                OfferFixtures.expectedOffer(
                        77895, "RTK", "89095555", 241, 3, true, "ACTIVE", null, false,
                        "2025-11-09T12:49:59Z", null,
                        OfferFixtures.expectedPlan(
                                4, "Минимальное мобильное приложение Домофона",
                                "Добавить/удалить ключ\nОткрыть дверь по кнопке в МП",
                                "2020-02-18T08:12:02Z", "2024-11-30T11:56:27.853925Z", null,
                                List.of("INTERCOM_USE", "INTERCOM_OPEN", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        33775, "COMPANY", "89095555", 558, 7, true, "ACTIVE", null, false,
                        "2025-10-09T12:47:07Z", null,
                        OfferFixtures.expectedPlan(
                                8, "Умный домофон",
                                "Получение видеозвонка с домофона на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр архива камер домофона\nВременные коды для гостей и курьеров\nИстория событий",
                                "2021-03-18T08:17:08Z", "2023-06-06T08:48:15.081316Z", null,
                                List.of("INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CAMERA_USE", "INTERCOM_CAMERA_ARCHIVE", "INTERCOM_EVENT_USE", "INTERCOM_CALLREDIRECT_APP")
                        )
                ),
                OfferFixtures.expectedOffer(
                        99964, "COMPANY", "89095555", 999, 3, false, "ACTIVE", null, false,
                        "2025-10-09T12:47:06Z", null,
                        OfferFixtures.expectedPlan(
                                3, "Обслуживание домофона", "Обслуживание домофона",
                                "2021-03-18T08:09:08Z", "2023-05-11T09:15:10.443316Z", null,
                                List.of()
                        )
                ),
                OfferFixtures.expectedOffer(
                        11111, "COMPANY", "89095555", 2222, 27, true, "ACTIVE", null, false,
                        "2025-10-09T05:47:05Z", null,
                        OfferFixtures.expectedPlan(
                                25, "Распознавание лиц_1", "FR для схемы B2B, все МРФ",
                                "2023-04-13T14:24:37Z", "2023-12-18T07:41:56.327099Z", null,
                                List.of("FR_USE", "FR_USE", "FR_USE", "FR_USE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        44444, "COMPANY", "89095555", 9999, 8, true, "ACTIVE", null, false,
                        "2025-10-09T05:45:50Z", null,
                        OfferFixtures.expectedPlan(
                                8, "Трубка", null,
                                "2021-03-18T08:28:29Z", "2022-11-16T09:53:52.106840Z", null,
                                List.of("INTERCOM_CALLREDIRECT_SIP")
                        )
                ),
                OfferFixtures.expectedOffer(
                        99999, "SHARED", "89095555", 1111, 26, false, "ACTIVE", 5000.0, true,
                        "2025-10-08T10:45:35Z", null,
                        OfferFixtures.expectedPlan(
                                26, "Комфортная подписка 3.0",
                                "Получение видеозвонка с домофона на приложение\nОткрытие двери домофона из приложения или виджета\nПросмотр камер домофона онлайн и в архиве\nВременные коды для гостей и курьеров\nИстория событий",
                                "2022-02-12T19:48:03Z", "2029-10-01T07:23:49.916902Z", null,
                                List.of("INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CAMERA_USE", "INTERCOM_CAMERA_ARCHIVE", "INTERCOM_EVENT_USE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        77777, "RTK", "89095555", 2222, 25, true, "ACTIVE", null, false,
                        "2024-10-08T10:44:07Z", null,
                        OfferFixtures.expectedPlan(
                                25, "Цифровой домофон минимальный доступ (аудиозвонок в МП)",
                                "Добавить/удалить ключ\nОткрыть дверь по кнопке в МП \nЗвонок в МП БЕЗ видео",
                                "2022-02-12T14:12:46Z", "2025-10-30T11:56:55.526598Z", null,
                                List.of("INTERCOM_USE", "INTERCOM_OPEN", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE", "INTERCOM_CALLREDIRECT_APP")
                        )
                ),
                OfferFixtures.expectedOffer(
                        33559, "COMPANY", "89095555", 1117, 27, true, "ACTIVE", null, false,
                        "2026-10-08T08:57:54Z", null,
                        OfferFixtures.expectedPlan(
                                27, "Распознавание лиц_1", "FR для схемы B2B, все МРФ",
                                "2023-04-13T14:24:37Z", "2023-12-18T07:41:56.327099Z", null,
                                List.of("FR_USE", "FR_USE", "FR_USE", "FR_USE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        55558, "COMPANY", "89095555", 1498, 17, true, "ACTIVE", null, false,
                        "2026-10-08T06:33:31Z", null,
                        OfferFixtures.expectedPlan(
                                17, "Видеонаблюдение. Мобильное приложение+Архив", null,
                                "2021-03-18T16:58:48Z", "2024-06-06T09:35:39.878440Z", null,
                                List.of("CAMERA_USE", "CAMERA_ARCHIVE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        11123, "COMPANY", "89095555", 1443, 17, true, "ACTIVE", null, false,
                        "2026-10-08T06:09:42Z", null,
                        OfferFixtures.expectedPlan(
                                17, "Видеонаблюдение. Мобильное приложение+Архив", null,
                                "2021-03-18T16:58:48Z", "2024-06-06T09:35:39.878440Z", null,
                                List.of("CAMERA_USE", "CAMERA_ARCHIVE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        42444, "COMPANY", "89095555", 83, 17, true, "ACTIVE", null, false,
                        "2025-10-08T04:34:16Z", null,
                        OfferFixtures.expectedPlan(
                                17, "Видеонаблюдение. Мобильное приложение+Архив", null,
                                "2021-03-18T16:58:48Z", "2024-06-06T09:35:39.878440Z", null,
                                List.of("CAMERA_USE", "CAMERA_ARCHIVE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        34512, "COMPANY", "89095555", 83, 16, true, "ACTIVE", null, false,
                        "2016-10-08T04:34:16Z", null,
                        OfferFixtures.expectedPlan(
                                16, "Умный шлагбаум", null,
                                "2021-03-18T16:57:40Z", "2022-11-16T09:41:59.442349Z", null,
                                List.of("BARRIER_USE", "BARRIER_OPEN", "BARRIER_CODE_USE", "BARRIER_CODE_USE", "BARRIER_CODE_USE", "BARRIER_CODE_USE", "BARRIER_CAMERA_USE", "BARRIER_CAMERA_ARCHIVE", "BARRIER_EVENT_USE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        66552, "RTK", "89095555", 83, 90, true, "ACTIVE", null, false,
                        "2023-10-08T04:31:48Z", null,
                        OfferFixtures.expectedPlan(
                                90, "Минимальное мобильное приложение Домофона (B2C АСР Ключ)",
                                "Добавить/удалить ключ",
                                "2023-11-10T13:49:03Z", "2025-10-30T11:59:59.422640Z", null,
                                List.of("INTERCOM_USE", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        33333, "SHARED", "89095555", 43, 11, false, "ACTIVE", 40.0, true,
                        "2026-10-08T04:31:06Z", null,
                        OfferFixtures.expectedPlan(
                                91, "Личная подписка",
                                "Управление домофоном через приложение: аудиозвонки, открытие двери\nОбслуживание домофона",
                                "2023-11-10T13:52:52Z", "2025-07-16T13:49:22.134212Z", "Получится открыть дверь себе или гостям",
                                List.of("INTERCOM_OPEN", "INTERCOM_CALLREDIRECT_APP")
                        )
                ),
                OfferFixtures.expectedOffer(
                        66554, "SHARED", "89095555", 88, 99, false, "ACTIVE", 1102.0, true,
                        "2026-10-08T04:31:05Z", null,
                        OfferFixtures.expectedPlan(
                                99, "Максимальная подписка с трубкой",
                                "Управление домофоном через приложение: видеозвонки, открытие двери, архив камеры, временные коды, история событий и переадресация звонков\nОбслуживание домофона\nПрием звонков на трубку. Трубка и её установка оплачиваются отдельно: 8 800 301 05 50",
                                "2023-12-07T11:48:26Z", "2024-06-27T15:06:08.086674Z", "Все преимущества Умного домофона",
                                List.of("INTERCOM_OPEN", "INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CAMERA_USE", "INTERCOM_CAMERA_ARCHIVE", "INTERCOM_EVENT_USE", "INTERCOM_CALLREDIRECT_APP", "INTERCOM_CALLREDIRECT_PSTN", "INTERCOM_CALLREDIRECT_SIP")
                        )
                ),
                OfferFixtures.expectedOffer(
                        12345, "SHARED", "89095555", 66, 93, false, "ACTIVE", 60.0, true,
                        "2024-10-08T04:31:05Z", null,
                        OfferFixtures.expectedPlan(
                                93, "Стандартная подписка с трубкой",
                                "Управление домофоном через приложение: видеозвонки, открытие двери\nОбслуживание домофона\nПрием звонков на трубку. Трубка и ее установка оплачиваются отдельно: 8 800 301 05 50",
                                "2023-11-10T13:57:11Z", "2025-07-16T13:50:00.385389Z", "Получится открыть дверь себе или гостям и увидеть, кто пришел",
                                List.of("INTERCOM_OPEN", "INTERCOM_CAMERA_USE", "INTERCOM_CALLREDIRECT_APP", "INTERCOM_CALLREDIRECT_SIP")
                        )
                ),
                OfferFixtures.expectedOffer(
                        33333, "COMPANY", "89095555", 1234, 17, true, "ACTIVE", null, false,
                        "2023-10-07T12:41:17Z", null,
                        OfferFixtures.expectedPlan(
                                17, "Видеонаблюдение. Мобильное приложение+Архив", null,
                                "2021-03-18T16:58:48Z", "2024-06-06T09:35:39.878440Z", null,
                                List.of("CAMERA_USE", "CAMERA_ARCHIVE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        11111, "RTK", "89095555", 33, 90, true, "ACTIVE", null, false,
                        "2023-10-07T09:27:07Z", null,
                        OfferFixtures.expectedPlan(
                                90, "Минимальное мобильное приложение Домофона (B2C АСР Ключ)",
                                "Добавить/удалить ключ",
                                "2023-11-10T13:49:03Z", "2025-10-30T11:59:59.422640Z", null,
                                List.of("INTERCOM_USE", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE", "INTERCOM_RFID_USE")
                        )
                ),
                OfferFixtures.expectedOffer(
                        45678, "SHARED", "89095555", 55, 93, false, "ACTIVE", 50.0, true,
                        "2026-10-07T09:26:03Z", null,
                        OfferFixtures.expectedPlan(
                                93, "Стандартная подписка с трубкой",
                                "Управление домофоном через приложение: видеозвонки, открытие двери\nОбслуживание домофона\nПрием звонков на трубку. Трубка и ее установка оплачиваются отдельно: 8 800 301 05 50",
                                "2023-11-10T13:57:11Z", "2025-07-16T13:50:00.385389Z", "Получится открыть дверь себе или гостям и увидеть, кто пришел",
                                List.of("INTERCOM_OPEN", "INTERCOM_CAMERA_USE", "INTERCOM_CALLREDIRECT_APP", "INTERCOM_CALLREDIRECT_SIP")
                        )
                ),
                OfferFixtures.expectedOffer(
                        12345, "SHARED", "107335897", 21, 99, false, "ACTIVE", 1000.0, true,
                        "2028-10-07T09:26:02Z", null,
                        OfferFixtures.expectedPlan(
                                99, "Максимальная подписка с трубкой",
                                "Управление домофоном через приложение: видеозвонки, открытие двери, архив камеры, временные коды, история событий и переадресация звонков\nОбслуживание домофона\nПрием звонков на трубку. Трубка и её установка оплачиваются отдельно: 8 800 301 05 50",
                                "2023-12-07T11:48:26Z", "2024-06-27T15:06:08.086674Z", "Все преимущества Умного домофона",
                                List.of("INTERCOM_OPEN", "INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CODE_USE", "INTERCOM_CAMERA_USE", "INTERCOM_CAMERA_ARCHIVE", "INTERCOM_EVENT_USE", "INTERCOM_CALLREDIRECT_APP", "INTERCOM_CALLREDIRECT_PSTN", "INTERCOM_CALLREDIRECT_SIP")
                        )
                )
        );
    }
}
