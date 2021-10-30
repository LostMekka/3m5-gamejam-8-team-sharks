package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisProgressBar
import de.lostmekka.gamejam.teamsharks.data.GameState
import ktx.actors.txt
import ktx.scene2d.*
import ktx.scene2d.vis.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class GameplayUi(stage: Stage) {
    fun update(state: GameState) {
        awarenessBar.update(state.enemyAwareness)
        moneyLabel.update(state.money)
    }

    private val bribeButton: KVisTextButton
    private val awarenessBar: Awareness
    private val moneyLabel : Money

    init {
        stage.actors {
            visTable() {
                setSize(stage.width, stage.height)

                visTable {
                    it.expandX().fillX().row()
                    it.padTop(100f)

                    money(0) {
                        it.padRight(200f)
                    }.also { moneyLabel = it }
                    awareness {
                        it.padRight(200f)
                    }.also { awarenessBar = it }

                    visTextButton("Bribe!") {
                        also {
                            val height = (0 until this.rows).fold(0f) { acc, i ->
                                acc + getRowMinHeight(i)
                            }
                            val pad = (40f - height) / 2

                            pad(pad, 20f, pad, 20f)

                            bribeButton = it
                        }
                    }
                }

                this.add().expand().fill()
            }
        }
    }
}

/// Awareness widget

class Awareness(min: Float, max: Float, stepSize: Float, vertical: Boolean) :
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
inline fun <S> KWidget<S>.awareness(
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

class Money(value: Int) : VisLabel("₽ $value") {
    fun update(value: Int) {
        txt = "₽ $value"
    }
}

@Scene2dDsl
@OptIn(ExperimentalContracts::class)
inline fun <S> KWidget<S>.money(
    value: Int,
    init: (@Scene2dDsl VisLabel).(S) -> Unit = {}
): Money {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(Money(value), init)
}
