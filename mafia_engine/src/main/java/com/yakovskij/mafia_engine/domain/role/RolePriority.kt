package com.yakovskij.mafia_engine.domain.role
enum class RolePriority(val value: Int) {
    HIGH(3),   // самые важные роли (удаляются в последнюю очередь)
    MEDIUM(2), // средний приоритет
    LOW(1)     // удаляются первыми
}