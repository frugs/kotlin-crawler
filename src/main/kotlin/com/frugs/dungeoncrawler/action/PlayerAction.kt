package com.frugs.dungeoncrawler.action

class PlayerAction (val id: String) {
    class object {
        public val MOVE_MOUSE_LOCATION: PlayerAction = PlayerAction("MOVE_MOUSE_LOCATION")
        public val STOP: PlayerAction = PlayerAction("STOP")

        public val ids: Array<String?> = array<String?>(MOVE_MOUSE_LOCATION.id, STOP.id)
        public val values: List<PlayerAction> = listOf(MOVE_MOUSE_LOCATION, STOP)
        public fun valueOf(id: String?): PlayerAction? = values.find { it.id == id }
    }
}