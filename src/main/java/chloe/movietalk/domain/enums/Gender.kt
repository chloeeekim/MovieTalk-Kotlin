package chloe.movietalk.domain.enums

import chloe.movietalk.exception.global.InvalidGenderEnumValueException

enum class Gender {
    MALE,
    FEMALE,
    OTHER;

    companion object {
        fun from(genderStr: String?): Gender {
            return entries.find { it.name == genderStr }
                ?: throw InvalidGenderEnumValueException
        }
    }
}
