package com.frugs.dungeoncrawler.event.player

import com.frugs.dungeoncrawler.event.Interrupter
import com.frugs.dungeoncrawler.event.Interruptible
import com.jme3.math.Vector3f
import com.frugs.dungeoncrawler.game.Player
import com.frugs.dungeoncrawler.event.Event

class PlayerMove (
    val destination: Vector3f,
    val player: Player,
    val rotate: Boolean = true,
    override public val timeIssued: Long = System.currentTimeMillis()
) : Interruptible, Interrupter {

    var continueMoving: Boolean = true
    var facingDestination: Boolean = false

    override public val chain: Event
        get() = if (continueMoving) PlayerMove(destination, player, facingDestination, timeIssued) else PlayerStop()

    override public fun process(tpf: Float) {
        continueMoving = player.moveTowardsDestination(destination, tpf)
        facingDestination = if (rotate) player.rotateTowardsDestination(destination, tpf) else false
    }

    override public fun interruptibleBy(interruptors: List<Interrupter>): Boolean =
        interruptors.any { when (it) { is PlayerMove, is PlayerStop -> true else -> false } }
}