package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import de.lostmekka.gamejam.teamsharks.GameplayScreen
import de.lostmekka.gamejam.teamsharks.data.GameState
import de.lostmekka.gamejam.teamsharks.data.ResourceAmount
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import ktx.actors.onClick
import ktx.scene2d.vis.*
import de.lostmekka.gamejam.teamsharks.data.times
import de.lostmekka.gamejam.teamsharks.sprite.Sprites
import de.lostmekka.gamejam.teamsharks.util.GridPosition
import ktx.actors.*
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
                    visImage(sprites.resourceIcons.getValue(c.resourceType))
                    visLabel(c.resourceType.name) { it.expandX().fillX() }
                    visLabel(c.amount.toString()) { it.expandX().width(50f) }

                    visTextButton("1") {
                        onChange { onSellClicked(1 * c.resourceType) }
                        it.expandX().fillX().width(50f)
                        isDisabled = c.amount < 1
                    }
                    visTextButton("10") {
                        onClick { onSellClicked(10 * c.resourceType) }
                        it.expandX().fillX().width(50f)
                        isDisabled = c.amount < 10
                    }
                    visTextButton("100") {
                        onClick { onSellClicked(100 * c.resourceType) }
                        it.expandX().fillX().width(50f)
                        isDisabled = c.amount < 100
                    }
                    row()
                }
            }
        }
        .also { inventory = it }
    }

    private var emptyCells : HashMap<GridPosition, Actor> = hashMapOf()
    private var popup : Actor? = null
    fun renderEmptyCell(
        stage: Stage,
        pos: GridPosition,
        rect: Rectangle,
        buyOptions: List<GameplayScreen.BuyOption>,
        onBuyClicked: (GameplayScreen.BuyOption) -> Unit,
    ) {
        emptyCells[pos]?.let { stage -= it }

        stage += scene2d.visTable {
            align(Align.bottomLeft)
            setSize(rect.width, rect.height)
            setPosition(rect.x, rect.y)

            floatingGroup {
                setFillParent(true)

                visTextButton("Buy") {
                    setPosition(
                        (rect.width - width) / 2,
                        (rect.height - height) / 2,
                    )

                    onClick {
                        stage += scene2d.visDialog("Buy machine") {
                            centerPosition(stage.width, stage.height)
                            width = 200f
                            align(Align.topLeft)

                            for (option in buyOptions) {
                                visLabel(option.name) { it.padRight(10f) }
                                visLabel(option.cost.toString())  { it.padRight(10f) }
                                visTextButton("Buy") {
                                    onChange {
                                        onBuyClicked(option)
                                        popup?.also { stage -= it }
                                        popup = null

                                        emptyCells[pos]?.let { stage -= it }
                                        emptyCells.remove(pos)
                                    }
                                    isDisabled = !option.canAfford
                                    it.width(40f)
                                }
                                row()
                            }
                            visTextButton("Back") {
                                onClick {
                                    popup?.also { stage -= it }
                                    popup = null
                                }
                            }
                        }.also {
                            popup = it
                        }
                    }
                }
            }
        }.also { emptyCells[pos] = it }

        popup?.let {
            stage -= it
            stage += it
        }
    }
}


