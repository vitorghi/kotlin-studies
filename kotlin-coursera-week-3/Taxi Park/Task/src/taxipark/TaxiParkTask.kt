package taxipark

import kotlin.math.ceil
import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
//        this.allDrivers.filter { driver ->
//            this.trips.none { it.driver == driver }
//        }.toSet()

        // Optimal
        this.allDrivers - this.trips.map { it.driver }

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
//    if (minTrips == 0) return allPassengers
//
//    val allPassengersWithTrips = trips.flatMap(Trip::passengers)
//    val tripsByPassenger = allPassengersWithTrips.groupingBy { it }.eachCount()
//
//    return tripsByPassenger
//            .filterValues { trips -> trips >= minTrips }
//            .keys
    // Optimal
    return allPassengers
            .filter { p -> trips.count { p in it.passengers } >= minTrips }
            .toSet()

}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
//    val allTripsWithDriver = this.trips.filter { it.driver == driver }
//
//    val passengersByCount = allTripsWithDriver.flatMap { it.passengers }.groupingBy { it }.eachCount()
//
//    return passengersByCount.filter { (_, count) -> count >= 2 }.keys

    return trips
            .filter { trip -> trip.driver == driver }
            .flatMap(Trip::passengers).groupBy { it }
            .filterValues { group -> group.size >= 2 }
            .keys
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val (withDiscount, withoutDiscount) = this.trips.partition { it.discount != null }

    val passengersWithDiscountByTripCount = withDiscount
            .flatMap { it.passengers }
            .groupingBy { it }.eachCount()

    val passengersWithoutDiscountByTripCount = withoutDiscount
            .flatMap { it.passengers }
            .groupingBy { it }.eachCount()

    return this.allPassengers.filter { p ->
        passengersWithDiscountByTripCount[p] ?: 0 > passengersWithoutDiscountByTripCount[p] ?: 0
    }.toSet()

}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {

    fun calculateRange(duration: Int): IntRange {
        val start = (duration / 10) * 10
        val end = start + 9

        return start..end
    }

    val ranges = this.trips.map { calculateRange(it.duration) }

    return ranges
            .groupingBy { it }
            .eachCount()
            .maxBy { (_, v) -> v }
            ?.key

}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (this.trips.isEmpty()) return false

    val totalIncome = this.trips.sumByDouble(Trip::cost)

    val maximumIncomeAcceptable = totalIncome * 0.8
    val maximumDrivers = floor(this.allDrivers.size * 0.2).toInt()

    val tripsByDriver = this.trips.groupBy(Trip::driver)

    val tripsTotalByDriver = tripsByDriver
            .map { (_, trips) -> trips.sumByDouble(Trip::cost) }
            .sortedDescending()

    val totalForTopDrivers = tripsTotalByDriver
            .take(maximumDrivers)
            .sum()

    return totalForTopDrivers >= maximumIncomeAcceptable
}