package com.yakovskij.mafia_engine.domain

data class GameSettings(
    val roleCounts: MutableMap<RoleType, Int> = mutableMapOf(
//        RoleType.MAFIA to 2,
//        RoleType.DOCTOR to 1,
//        RoleType.DETECTIVE to 1,
//        RoleType.CIVILIAN to 4 // чтобы в сумме по умолчанию было 8
    )
) {

    fun getTotalRolesCount(): Int = roleCounts.values.sum()
    fun getTotalActiveRolesCount(): Int = roleCounts.filter { it.key != RoleType.CIVILIAN }.values.sum()

    fun getCountFor(role: RoleType): Int = roleCounts[role] ?: 0

    fun setCount(role: RoleType, count: Int) {
        if (count < 0) throw IllegalArgumentException("Count for $role cannot be negative")
        roleCounts[role] = count
    }

    fun allRolesList(): List<RoleType> = roleCounts.flatMap { (role, count) -> List(count) { role } }
}
