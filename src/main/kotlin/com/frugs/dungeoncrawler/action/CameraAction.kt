package com.frugs.dungeoncrawler.action

enum class CameraAction {
    public val id: String = this.toString()

    MOVE_LEFT
    MOVE_RIGHT
    MOVE_DOWN
    MOVE_UP

    public val CameraAction.ids: Array<String?> = CameraAction.values().map { it.id }.toArray() as Array<String?>
}
