package maikura.hellocomand

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.component.ItemAttributeModifiers

/**
 * アイテムの定義と登録。
 *
 * 注意: アセット/データの名前空間は "hellocommand"（m が 2 つ）。
 * fabric.mod.json の id は "hellocomand" だが、リソースパスはこちらに合わせている。
 */
object ModItems {
    const val MOD_ID = "hellocommand"

    // ---------------------------------------------------------------
    // 素材
    // ---------------------------------------------------------------

    /**
     * マルチツール用の素材。鉄ティア相当だが耐久とエンチャント適性を強化。
     * バニラの ToolMaterial.IRON は (INCORRECT_FOR_IRON_TOOL, 250, 6.0f, 2.0f, 14, IRON_TOOL_MATERIALS)。
     */
    val MULTI_TOOL_MATERIAL = ToolMaterial(
        BlockTags.INCORRECT_FOR_IRON_TOOL, // このタグのブロックはドロップしない（黒曜石など）
        500,                               // 耐久値
        6.0f,                              // 採掘速度
        2.0f,                              // 攻撃力ボーナス
        22,                                // エンチャント適性（高いほど良いエンチャントが出やすい）
        ItemTags.IRON_TOOL_MATERIALS,      // 修理素材（鉄インゴット）
    )

    // ---------------------------------------------------------------
    // アイテム
    // ---------------------------------------------------------------

    val CUSTOM_ITEM: Item = register("custom_item")

    val CUSTOM_WEAPON: Item = register("custom_weapon", tune = {
        attributes(weaponAttributes(attackDamage = 9.0, attackSpeed = 3.0))
        durability(500)
        enchantable(22)
    })

    /**
     * マルチツール: ツルハシ + 斧 + シャベルを 1 本に。
     *
     * tool() が以下をまとめて設定する:
     *  - TOOL コンポーネント（採掘可能ブロック = ModTags.Blocks.MINEABLE_WITH_MULTI_TOOL、速度、ドロップ可否）
     *  - 攻撃力 / 攻撃速度の属性（attackDamage には素材のボーナスが加算される）
     *  - 耐久 / 修理素材 / エンチャント適性（素材から）
     *  - WEAPON コンポーネント
     * 参考: 鉄ツルハシは (1.0f, -2.8f)、鉄の斧は (6.0f, -3.1f)。
     */
    val MULTI_TOOL: Item = register("multi_tool", factory = ::MultiToolItem, tune = {
        tool(
            MULTI_TOOL_MATERIAL,
            ModTags.Blocks.MINEABLE_WITH_MULTI_TOOL,
            4.0f,   // 攻撃力（+ 素材ボーナス 2.0）
            -2.9f,  // 攻撃速度（基準 4.0 に加算 → 1.1）
            0.0f,   // 攻撃時に相手の盾を無効化する秒数（斧は 5 秒）
        )
    })

    // ---------------------------------------------------------------
    // ヘルパー
    // ---------------------------------------------------------------

    private fun weaponAttributes(attackDamage: Double, attackSpeed: Double): ItemAttributeModifiers =
        ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND,
            )
            .add(
                Attributes.ATTACK_SPEED,
                AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND,
            )
            .build()

    /**
     * アイテムを生成してレジストリに登録する。
     * @param tune    Item.Properties への追加設定
     * @param factory Properties からアイテムを作る関数（デフォルトは素の Item）
     */
    private fun register(
        name: String,
        factory: (Item.Properties) -> Item = ::Item,
        tune: Item.Properties.() -> Unit = {},
    ): Item {
        val key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name))
        val properties = Item.Properties().setId(key).apply(tune)
        return Registry.register(BuiltInRegistries.ITEM, key, factory(properties))
    }

    // ---------------------------------------------------------------
    // 初期化（クリエイティブタブへの追加）
    // ---------------------------------------------------------------

    fun initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register { it.accept(CUSTOM_ITEM) }
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register { it.accept(CUSTOM_WEAPON) }
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register { it.accept(MULTI_TOOL) }
    }
}
