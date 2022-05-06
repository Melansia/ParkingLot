package com.example.parkinglot

fun main() {
    var carPark = ParkingLot(0)
    while (true) {
        val action = readln().replace("_", " ").split(" ")

        when (action.first()) {
            "exit" -> break
            "create" -> {
                carPark = ParkingLot(action[1].toInt())
                println("Created a parking lot with ${action[1]} spots.")
            }
            "park" -> carPark.park(Car(action[1], action[2].capitalize()))
            "leave" -> carPark.leave(action[1].toInt())
            "status" -> carPark.status()
            "spot", "reg" -> carPark.carSpotting(action[0], action[2], action[3])
        }
    }
}


data class Car(val regNumber: String?, val color: String?)

class ParkingLot(size: Int) {
    private val parkingLot = MutableList<Car?>(size) { null }
    private val freeParingSpot = MutableList(size) { true }

    fun park(car: Car) {
        if (parkingLot.size < 1) return println("Sorry, a parking lot has not been created.")

        val freeSpot = freeParingSpot.indexOf(true)
        if (freeSpot == -1) {
            println("Sorry, the parking lot is full.")
        } else {
            parkingLot[freeSpot] = car
            freeParingSpot[freeSpot] = false
            println("${car.color} car parked in spot ${freeSpot + 1}.")
        }
    }

    fun leave(spot: Int) {
        if (false !in freeParingSpot && parkingLot.size > 0) return println("Parking lot is empty.")
        if (freeParingSpot.size < 1) return println("Sorry, a parking lot has not been created.")
        if (freeParingSpot[spot - 1]) {
            println("There is no car in spot $spot.")
        } else {
            try {
                parkingLot[spot - 1] = Car(null, null)
                freeParingSpot[spot - 1] = true
                println("Spot $spot is free.")
            }catch (e: IndexOutOfBoundsException){
                println("There spot nr.$spot. doesn't exist!")
            }
        }
    }

    fun status() {
        if (false !in freeParingSpot && parkingLot.size > 0) return println("Parking lot is empty.")
        if (freeParingSpot.size < 1) return println("Sorry, a parking lot has not been created.")
        try {
            val occupied = freeParingSpot.indexOfFirst { true }
            var firstOccupied = occupied
            for (i in 0..parkingLot.lastIndex) {
                if (parkingLot[firstOccupied]?.regNumber == null) {
                    firstOccupied++
                } else {
                    println("${firstOccupied + 1} ${parkingLot[firstOccupied]?.regNumber} ${parkingLot[firstOccupied]?.color}")
                    firstOccupied++
                }
            }
        } catch (e: Error) {
            println("Status ${e.message}")
        }
    }

    fun carSpotting(action: String, by: String, parameter: String) {
        if (freeParingSpot.size < 1) return println("Sorry, a parking lot has not been created.")
        if (action == "spot" && by == "color") findSpotByColor(parameter.uppercase())
        if (action == "spot" && by == "reg") findSpotByReg(parameter)
        if (action == "reg" && by == "color") findRegByColor(parameter.uppercase())
    }

    private fun findRegByColor(color: String) {
        val result = mutableSetOf<String>()
        val occupied = freeParingSpot.indexOfFirst { true }
        var firstOccupied = occupied

        for (i in 0..parkingLot.lastIndex) {
            if (parkingLot[firstOccupied]?.color?.uppercase() == color) {
                result.add("${parkingLot[firstOccupied]?.regNumber}")
            }
            firstOccupied++
        }
        if (result.isEmpty()) {
            println("No cars with color ${color.uppercase()} were found.")
        } else {
            println(result.joinToString(", "))
        }
    }

    private fun findSpotByColor(color: String) {

        val result = mutableSetOf<Any>()
        val occupied = freeParingSpot.indexOfFirst { true }
        var firstOccupied = occupied

        for (i in 0..parkingLot.lastIndex) {
            if (parkingLot[firstOccupied]?.color?.uppercase() == color) {
                result.add(firstOccupied + 1)
            }
            firstOccupied++
        }
        if (result.isEmpty()) {
            println("No cars with color ${color.uppercase()} were found.")
        } else {
            println(result.joinToString(", "))
        }
    }

    private fun findSpotByReg(regNumber: String) {

        val occupied = freeParingSpot.indexOfFirst { true }
        var firstOccupied = occupied

        var result = ""
        for (i in 0..parkingLot.lastIndex) {
            if (parkingLot[firstOccupied]?.regNumber == regNumber) {
                result = "${firstOccupied + 1}"
            }
            firstOccupied++
        }
        if (result.isEmpty()) {
            println("No cars with registration number $regNumber were found.")
        } else {
            println(result)
        }
    }
}