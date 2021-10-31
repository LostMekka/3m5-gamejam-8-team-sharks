package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisTable
import de.lostmekka.gamejam.teamsharks.data.GameState
import de.lostmekka.gamejam.teamsharks.data.ResourceAmount
import ktx.actors.onClick
import ktx.scene2d.vis.*
import de.lostmekka.gamejam.teamsharks.data.times
import de.lostmekka.gamejam.teamsharks.sprite.Sprites
import ktx.actors.minusAssign
import ktx.actors.plusAssign
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane

class GameplayUi {
    private var staticUi : Actor? = null
    fun renderStaticUi(stage: Stage, state: GameState, onBribeClicked: () -> Unit, sprites: Sprites) {
        staticUi?.let { stage -= it }
        stage += createStaticUi(stage, state, onBribeClicked, sprites).also { staticUi = it }
    }

    private var inventory : Actor? = null
    fun renderInventory(stage: Stage, rect: Rectangle, content: List<ResourceAmount>, onSellClicked: (ResourceAmount) -> Unit, sprites: Sprites) {
        inventory?.let { stage -= it }
        stage += scene2d.scrollPane {
            setPosition(rect.x, rect.y)
            setSize(rect.width, rect.height)

            visTable(true) {
                pad(10f)

                visLabel("Inventory") { it.colspan(3).expandX().fillX() }
                visLabel("Sell") { it.colspan(3).expandX().fillX() }
                row()

                for (c in content) {
                    sprites.resourceIcons[c.resourceType]
                        ?.let { visImage(it) }
                        ?: visLabel("")

                    visLabel(c.resourceType.name) { it.expandX().fillX() }
                    visLabel(c.amount.toString()) { it.expandX().width(50f) }

                    visTextButton("1") {
                        onClick { onSellClicked(1 * c.resourceType) }
                        it.expandX().fillX().width(50f)
                    }
                    visTextButton("10") {
                        onClick { onSellClicked(10 * c.resourceType) }
                        it.expandX().fillX().width(50f)
                    }
                    visTextButton("100") {
                        onClick { onSellClicked(100 * c.resourceType) }
                        it.expandX().fillX().width(50f)
                    }
                    row()
                }
            }
        }
        .also { inventory = it }
    }
}


