package com.frugs.dungeoncrawler.action

class CameraAction(val id: String) {
    class object {
        public val MOVE_LEFT: CameraAction = CameraAction("MOVE_LEFT")
        public val MOVE_RIGHT: CameraAction = CameraAction("MOVE_RIGHT")
        public val MOVE_DOWN: CameraAction = CameraAction("MOVE_DOWN")
        public val MOVE_UP: CameraAction = CameraAction("MOVE_UP")

        public val ids: Array<String?> = array<String?>(MOVE_LEFT.id, MOVE_RIGHT.id, MOVE_DOWN.id, MOVE_UP.id)
        public fun values(): List<CameraAction> = array(MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN, MOVE_UP).toList()
        public fun valueOf(id: String?): CameraAction? = values().find { it.id == id }
    }
}
