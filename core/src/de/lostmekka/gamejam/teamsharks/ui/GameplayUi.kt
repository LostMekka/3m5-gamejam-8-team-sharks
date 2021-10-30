package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisTable
import de.lostmekka.gamejam.teamsharks.data.GameState
import de.lostmekka.gamejam.teamsharks.data.ResourceAmount
import ktx.actors.onClick
import ktx.scene2d.vis.*
import de.lostmekka.gamejam.teamsharks.data.times
import ktx.actors.minusAssign
import ktx.actors.plusAssign
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane
import ktx.scene2d.table

class GameplayUi {
    private var staticUi : Actor? = null
    fun renderStaticUi(stage: Stage, state: GameState) {
        staticUi?.let { stage -= it }
        stage += createStaticUi(stage, state).also { staticUi = it }
    }

    private var inventory : Actor? = null
    fun renderInventory(stage: Stage, rect: Rectangle, content: List<ResourceAmount>, onSellClicked: (ResourceAmount) -> Unit) {
        inventory?.let { stage -= it }
        stage += scene2d.scrollPane {
            setPosition(rect.x, rect.y)
            setSize(rect.width, rect.height)

            visTable(true) {
                pad(10f)

                visLabel("Inventory")
                row()

                for (a in content) {
                    visLabel(a.resourceType.name) { it.expandX().fillX() }
                    visLabel(a.amount.toString()) { it.expandX().width(50f) }

                    visTextButton("1") {
                        onClick { onSellClicked(1 * a.resourceType) }
                        it.expandX().fillX().width(50f)
                    }
                    visTextButton("10") {
                        onClick { onSellClicked(10 * a.resourceType) }
                        it.expandX().fillX().width(50f)
                    }
                    visTextButton("100") {
                        onClick { onSellClicked(100 * a.resourceType) }
                        it.expandX().fillX().width(50f)
                    }
                    row()
                }

//                visTable { setFillParent(true) }
            }
        }
        .also { inventory = it }
    }
}


