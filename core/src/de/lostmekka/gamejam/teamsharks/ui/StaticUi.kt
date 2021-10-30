package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisProgressBar
import de.lostmekka.gamejam.teamsharks.data.GameState
import ktx.actors.txt
import ktx.scene2d.*
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun createStaticUi(stage: Stage, state: GameState): Actor {
    return scene2d.visTable() {
        setSize(stage.width, stage.height)

        visTable {
            it.expandX().fillX().row()
            it.padTop(10f)

            money(state.money) {
                it.padRight(200f)
            }
            awareness(state.enemyAwareness) {
                it.padRight(200f)
            }

            visTextButton("Bribe!") {
                also {
                    val height = (0 until this.rows).fold(0f) { acc, i ->
                        acc + getRowMinHeight(i)
                    }
                    val pad = (40f - height) / 2

                    pad(pad, 20f, pad, 20f)
                }
            }
        }

        this.add().expand().fill()
    }
}


/// Awareness widget

private class Awareness(min: Float, max: Float, stepSize: Float, vertical: Boolean) :
    VisProgressBar(min, max, stepSize, vertical) {

    fun update(value: Float) {
        this.value = value
    }

    override fun getPrefWidth(): Float {
        return 400f
    }
}

@Scene2dDsl
@OptIn(ExperimentalContracts::class)
private inline fun <S> KWidget<S>.awareness(
    min: Float = 0f,
    max: Float = 1f,
    step: Float = 0.001f,
    vertical: Boolean = false,
    init: (@Scene2dDsl VisProgressBar).(S) -> Unit = {}
): Awareness {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(Awareness(min, max, step, vertical), init)
}

/// Money widget

private class Money(value: Int) : VisLabel("₽ $value") {
    override fun getPrefWidth(): Float = 200f

    fun update(value: Int) {
        txt = "₽ $value"
    }
}

@Scene2dDsl
@OptIn(ExperimentalContracts::class)
private inline fun <S> KWidget<S>.money(
    value: Int,
    init: (@Scene2dDsl VisLabel).(S) -> Unit = {}
): Money {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(Money(value), init)
}
