package chloe.movietalk.domain.enums

import chloe.movietalk.exception.CustomException
import chloe.movietalk.exception.global.InvalidRoleEnumValueException
import java.util.*
import java.util.function.Supplier

enum class UserRole {
    ADMIN,
    USER;

    companion object {
        fun from(role: String?): UserRole? {
            if (role == null) return null
            return Arrays.stream<UserRole?>(entries.toTypedArray())
                .filter { g: UserRole? -> g!!.name == role }
                .findFirst()
                .orElseThrow<CustomException?>(Supplier { InvalidRoleEnumValueException.EXCEPTION })
        }
    }
}
