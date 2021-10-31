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
    fun renderStaticUi(
        stage: Stage,
        sprites: Sprites,
        currentDepth: Float,
        money: Int,
        awareness: Float,
        bribeCost: Int,
        onBribeClicked: () -> Unit,
    ) {
        staticUi?.let { stage -= it }
        stage += createStaticUi(
            stage,
            sprites,
            currentDepth,
            money,
            awareness,
            bribeCost,
            onBribeClicked,
        ).also { staticUi = it }
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
                            width = 200f
                            height = (2+ buyOptions.size) * 33f
                            centerPosition(stage.width, stage.height)

                            contentTable += visTable(defaultSpacing = true) {
                                setFillParent(true)
                                align(Align.topLeft)

                                for (option in buyOptions) {
                                    visLabel(option.name) {
                                        it.align(Align.left)
                                    }
                                    visLabel(option.cost.toString()) {
                                        it.align(Align.right)
                                    }
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
                            }

                            buttonsTable += visTextButton("Back") {
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
    fun renderHelpMenu(
        stage: Stage,
    ) {
        stage += scene2d.visTable {
            setPosition(40f, stage.height - 30f)

            visTextButton("Story\nand Help") {
                onClick {
                    stage += scene2d.visDialog("Story and Help") {
                        centerPosition(stage.width, stage.height)
                        width = 500f
                        height = 500f
                        contentTable += visTextArea {
                            setFillParent(true)
                            text =
                            """
                            Hello and welcome to this game with weird and cruel world!
                            
                            The story is as follows: You have build a moving factory, which has a quarry below.
                            This quarry can automatically dig the ground and extract several ores from the earth chunks.
                            Suddenly there are other species on the planet besides you, they can hear and feel, what your factory is doing.
                            Luckily for you - you can pay them with some roubles, so they will be less worried about your stuff.
                            
                            So, sell, what your factory produces, buy new machines, upgrade them, dig into the depths below
                            and don't forget to Bribe with some roubles to support corruption and dictate your rules!
                            """.trimIndent()
                        }

                        visTextButton("Back") {
                            align(Align.topLeft)
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

        popup?.let {
            stage -= it
            stage += it
        }
    }
}


