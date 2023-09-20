package com.moove.shared

import com.github.javafaker.Faker

val faker = object : Faker() {}

fun <T> randomListOf(
    minListElements: Int = 2,
    maxListElements: Int = 20,
    elementGenerationBlock: () -> T
): List<T> {
    val numberOfElements = faker.number().numberBetween(minListElements, maxListElements)
    return (0..numberOfElements).map { elementGenerationBlock() }
}

fun <E> Array<E>.random() = this[faker.number().numberBetween(0, this.size)]

fun <T> randomNullableOf(nullableValueGenerationBlock: () -> T): T? {
    if (faker.bool().bool()) {
        return null
    }
    return nullableValueGenerationBlock()
}
