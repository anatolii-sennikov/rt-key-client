package io.github.anatoliisennikov.rtkey.model;

/**
 * A flat with the building, entrance and floor data denormalised onto it.
 *
 * <p>Returned as part of {@link User}.
 *
 * @param id             flat identifier
 * @param number         flat number
 * @param buildingId     building identifier
 * @param orponId        ORPON identifier of the building
 * @param buildingPlace  building place
 * @param buildingStreet building street
 * @param entranceId     entrance identifier
 * @param entranceName   entrance name
 * @param floorId        floor identifier
 * @param floorName      floor name
 */
public record Flat(
        Integer id,
        String number,
        Integer buildingId,
        Integer orponId,
        String buildingPlace,
        String buildingStreet,
        String entranceId,
        String entranceName,
        String floorId,
        String floorName
) {
}
