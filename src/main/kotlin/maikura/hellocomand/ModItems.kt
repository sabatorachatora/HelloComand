package maikura.hellocomand

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityAttachment
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.Tool
import net.minecraft.world.item.component.Weapon
import org.objectweb.asm.Attribute
import kotlin.reflect.KProperty
import net.minecraft.core.HolderSet
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block






object ModItems {
    const val MOD_ID = "hellocommand"

    val CUSTOM_ITEM: Item = registar("custom_item") { properties -> Item(properties) }
    val CUSTOM_WEAPON: Item = registar("custom_weapon",
        tune = {
            attributes(weaponAttributes(attackDamage = 9.0, attackSpeed = 3.0))
            durability(500)
            enchantable(22)
        } ) { properties -> Item(properties) }

    val CUSTOM_TOOL: Item = registar("multi_tool",
        tune = {
            attributes(multiToolAttributes())
            durability(500)
            enchantable(22)
        }
        ) { properties -> Item(properties)}

    private fun multiToolAttributes(): ItemAttributeModifiers =
            ItemAttributeModifiers.builder()
                .add(
                    Attributes.BLOCK_BREAK_SPEED,
                    AttributeModifier(
                        Identifier.fromNamespaceAndPath("hellocomand", "mining_speed"),
                        5.4,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND // メインハンド保持時のみ適用
                )
                .add(
                    Attributes.ATTACK_DAMAGE,
                    AttributeModifier(
                        Identifier.fromNamespaceAndPath("hellocomand", "attack_damage"),
                        5.4,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND // メインハンド保持時のみ適用
                )
                .add(
                    Attributes.ATTACK_SPEED,
                    AttributeModifier(
                        Identifier.fromNamespaceAndPath("hellocomand", "attack_speed"),
                        1.0,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND // メインハンド保持時のみ適用
                )
                .add(
                    Attributes.ATTACK_SPEED,
                    AttributeModifier(
                        Identifier.fromNamespaceAndPath("hellocomand", "attack_speed"),
                        1.0,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND // メインハンド保持時のみ適用
                )
            .build()


    private fun weaponAttributes(attackDamage: Double, attackSpeed: Double): ItemAttributeModifiers =
        ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_SPEED,
                AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
            )
            .build()


    private fun registar(
        name: String,
        tune:  Item.Properties.() -> Unit ={},
        factory: (Item.Properties) -> Item): Item {
        val id = Identifier.fromNamespaceAndPath(MOD_ID, name)
        val key = ResourceKey.create(Registries.ITEM, id)

        val properties = Item.Properties().setId(key)
        tune(properties)

        val item = factory(properties)

        return Registry.register(BuiltInRegistries.ITEM, key, item)
    }

    fun initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register { entries ->
            entries.accept(CUSTOM_ITEM)

        }
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register { entries ->
            entries.accept(CUSTOM_WEAPON)
        }
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register { entries ->
            entries.accept(CUSTOM_TOOL)
        }
    }
}