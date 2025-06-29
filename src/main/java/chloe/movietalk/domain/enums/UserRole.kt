package chloe.movietalk.domain.enums

import chloe.movietalk.exception.global.InvalidRoleEnumValueException

enum class UserRole {
    ADMIN,
    USER;

    companion object {
        fun from(role: String?): UserRole {
            return entries.find { it.name == role }
                ?: throw InvalidRoleEnumValueException.EXCEPTION
        }
    }
}
