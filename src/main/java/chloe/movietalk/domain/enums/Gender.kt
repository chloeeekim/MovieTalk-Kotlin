package chloe.movietalk.domain.enums

import chloe.movietalk.exception.CustomException
import chloe.movietalk.exception.global.InvalidGenderEnumValueException
import java.util.*
import java.util.function.Supplier

enum class Gender {
    MALE,
    FEMALE,
    OTHER;

    companion object {
        fun from(genderStr: String?): Gender? {
            if (genderStr == null) return null
            return Arrays.stream<Gender?>(entries.toTypedArray())
                .filter { g: Gender? -> g!!.name == genderStr }
                .findFirst()
                .orElseThrow<CustomException?>(Supplier { InvalidGenderEnumValueException.EXCEPTION })
        }
    }
}
