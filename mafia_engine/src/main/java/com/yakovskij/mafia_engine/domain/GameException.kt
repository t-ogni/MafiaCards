package com.yakovskij.mafia_engine.domain

sealed class GameException(message: String) : Exception(message) {

    class PlayerCountMismatch(
        expected: Int,
        actual: Int
    ) : GameException("Количество игроков ($actual) не соответствует ожидаемому числу игроков ($expected) для выбранных настроек.")

    class InsufficientRoles(
        required: Int,
        available: Int
    ) : GameException("Недостаточно ролей: требуется $required, а доступно $available.")

    class DuplicatePlayerId(playerId: Int) : GameException("Игрок с ID #$playerId уже существует. ID должны быть уникальными.")

    class InvalidRoleCount(role: String, count: Int) : GameException("Неверное количество ролей '$role': $count. Должно быть >= 0.")

    class PlayerNotFound() : GameException("Игрок не найден")

    class GameNotSetup() : GameException("Игра не была корректно инициализирована. Выполните setupGame прежде, чем начать.")

    class InvalidTarget() : GameException("Выбранная жертва не существует в игре")

    class TargetIsDead() : GameException("Выбрали в качестве жертвы мёртвого игрока")

    class DeadPlayerAction() : GameException("Мёртвый игрок не может взаимодействовать")


}