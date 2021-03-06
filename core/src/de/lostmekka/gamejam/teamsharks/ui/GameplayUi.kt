package de.lostmekka.gamejam.teamsharks.ui

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisProgressBar
import de.lostmekka.gamejam.teamsharks.GameplayScreen
import de.lostmekka.gamejam.teamsharks.data.ResourceAmount
import de.lostmekka.gamejam.teamsharks.data.ResourceType
import ktx.actors.onClick
import ktx.scene2d.vis.*
import de.lostmekka.gamejam.teamsharks.data.times
import de.lostmekka.gamejam.teamsharks.sprite.Sprites
import de.lostmekka.gamejam.teamsharks.util.GridPosition
import ktx.actors.*
import ktx.scene2d.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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
                    visLabel(c.resourceType.toString()) { it.expandX().fillX() }
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

    private var cells : HashMap<GridPosition, Actor> = hashMapOf()
    private var popup : Actor? = null
    fun renderEmptyCell(
        stage: Stage,
        pos: GridPosition,
        rect: Rectangle,
        buyOptions: List<GameplayScreen.BuyOption>,
        onBuyClicked: (GameplayScreen.BuyOption) -> Unit,
    ) {
        cells[pos]?.let { stage -= it }

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

                                            this@GameplayUi.cells[pos]?.let { stage -= it }
                                            this@GameplayUi.cells.remove(pos)
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
        }.also { cells[pos] = it }

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
                        width = 500f
                        height = 420f
                        pad(20f, 20f, 0f, 20f)
                        centerPosition(stage.width, stage.height)

                        contentTable += visLabel("") {
                            it.pad(30f, 0f, 0f, 0f).fillY()
                            setFillParent(true)
                            wrap = true
                            txt = """
                            Welcome to this untitled stealth factory game!
                            
                            You have built a moving factory that drills itself through the rock inside a holy crater.
                            The factory automatically mines material from the resource deposits that you drill by.
                            Everything your factory does will increase the public's awareness and if the awareness meter fills up, you are caught and the game is over.
                            Luckily for you, you can bribe the people who notice you with some roubles, so they will be silent for a while longer.
                            
                            So sell, what your factory produces, buy new machines, upgrade them and dig into the depths below!
                            """.trimIndent()
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

        popup?.let {
            stage -= it
            stage += it
        }
    }

    fun renderMachineCell(
        stage: Stage,
        pos: GridPosition,
        rect: Rectangle,
        machine: GameplayScreen.MachineStatus,
        onUpgradeClicked: () -> Unit
    ) {
        cells[pos]?.let { stage -= it }

        stage += scene2d.visTable {
            align(Align.bottomLeft)
            setSize(rect.width, rect.height)
            setPosition(rect.x, rect.y)

            floatingGroup {
                setFillParent(true)

                machineProgress {
                    x = 74f
                    height = rect.height - 1
                    value = machine.workProgress
                    isVisible = machine.isWorking
                }

                visTable {
                    x = 110f
                    y = rect.height / 2

                    visLabel("Tier ${machine.tier}") {
                        it.align(Align.right)
                    }
                    row()

                    visLabel("${machine.upgradeCost}") {
                        it.align(Align.right)
                        isVisible = machine.upgradeAvailable
                    }
                    row()

                    visTextButton("Up!") {
                        it.align(Align.right)
                        isDisabled = !machine.upgradeAffordable
                        isVisible = machine.upgradeAvailable

                        onChange { onUpgradeClicked() }
                    }
                }
            }
        }.also { cells[pos] = it }
    }

    private var resources : MutableList<Actor> = mutableListOf()
    fun cleanupResourceDeposits(stage: Stage) {
        for (resource in resources) {
            stage -= resource
        }
    }
    fun renderResourceDeposit(
        stage: Stage,
        sprites: Sprites,
        rect: Rectangle,
        resourceType: ResourceType,
        resourceAmount: Int
    ) {
        stage += scene2d.visTable {
            align(Align.bottomLeft)
            setSize(rect.width, rect.height)
            setPosition(rect.x, rect.y)

            sprites.resourceIcons[resourceType]?.let { visImage(it) } ?: visLabel("")
            row()

            visLabel(resourceType.toString()) {
                it.fillX().expandX()
                setAlignment(Align.center)
            }
            row()

            visLabel("$resourceAmount left") {
                it.fillX().expandX().padBottom(6f)
                setAlignment(Align.center)
            }
        }.also { resources.add(it) }
    }
}

/// machine progress widget

private class MachineProgress(width: Float) :
    VisProgressBar(0f, 1f, 0.001f, true) {

//    val explicitWidth = width
//    override fun getPrefWidth() = explicitWidth
}

@Scene2dDsl
@OptIn(ExperimentalContracts::class)
private inline fun <S> KWidget<S>.machineProgress(
    width: Float = 4f,
    init: (@Scene2dDsl VisProgressBar).(S) -> Unit = {}
): MachineProgress {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return actor(MachineProgress(width), init)
}
