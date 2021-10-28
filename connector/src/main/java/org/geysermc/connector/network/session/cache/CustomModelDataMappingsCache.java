/*
 * Copyright (c) 2019-2021 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.connector.network.session.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.registry.type.CustomModelDataItemMapping;
import org.geysermc.connector.registry.type.ItemMappings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomModelDataMappingsCache {

    private GeyserSession session;
    private List<StartGamePacket.ItemEntry> bedrockCustomItems;
    private List<ComponentItemData> itemDataList;
    private List<CustomModelDataItemMapping> customModelDataItemMappings;
    private static Map<String, Map<Integer, Integer>> itemTypeToCustomModelDataMap = new HashMap<>();

    public CustomModelDataMappingsCache(GeyserSession session) {
        this.session = session;
    }
    public void loadMappingsFile() throws IOException {
        if (GeyserConnector.getInstance().getConfig().isUseCustomModelDataMappings()) {
            File mappingsFile = GeyserConnector.getInstance().getBootstrap().getConfigFolder().resolve("mappings.json").toFile();
            customModelDataItemMappings = new ArrayList<>();
            bedrockCustomItems = new ArrayList<>();
            itemDataList = new ArrayList<>();
            if (mappingsFile.exists()) {
                JsonNode mappingsNode = null;
                try {
                    mappingsNode = GeyserConnector.JSON_MAPPER.readTree(mappingsFile);
                } catch (Exception e) {
                    GeyserConnector.getInstance().getLogger().warning("There are something wrong in your mappingsNode.json.");
                }
                assert mappingsNode != null;
                ItemMappings mappings = session.getItemMappings();

                Map<Integer, Integer> customModelDataToBedrockId = new HashMap<>();

                int customItemId = mappings.getItems().size();
                for(JsonNode mappingNode : mappingsNode) {
                    customItemId ++;
                    CustomModelDataItemMapping mapping = new CustomModelDataItemMapping();
                    mapping.setCustomModelData(mappingsNode.get("custom_model_data").intValue());
                    mapping.setItemType(mappingsNode.get("item_type").textValue());
                    mapping.setTextureData(mappingsNode.get("texture_data").textValue());
                    mapping.setItemId(customItemId);

                    ComponentItemData itemData = buildSimpleCustomItem(mapping);
                    itemDataList.add(itemData);
                    new StartGamePacket.ItemEntry("geysermc:" + mapping.getItemType() + mapping.getTextureData() + mapping.getItemId(), (short) mapping.getItemId());
                    customModelDataItemMappings.add(mapping);

                    customModelDataToBedrockId.put(mapping.getCustomModelData(), customItemId);
                    itemTypeToCustomModelDataMap.put(mapping.getItemType(), customModelDataToBedrockId);

                }
            } else {
                mappingsFile.createNewFile();
            }
        }
    }

    public List<CustomModelDataItemMapping> getCustomModelDataItemMappings() {
        return customModelDataItemMappings;
    }

    public List<ComponentItemData> getItemDataList() {
        return itemDataList;
    }

    public static Map<String, Map<Integer, Integer>> getItemTypeToCustomModelDataMap() {
        return itemTypeToCustomModelDataMap;
    }

    private ComponentItemData buildSimpleCustomItem(CustomModelDataItemMapping mapping) {

        int customModelData = mapping.getCustomModelData();
        int itemId = mapping.getItemId();
        String itemType = mapping.getItemType();
        String textureData = mapping.getTextureData();

        ComponentItemData customItemData = null;

        // Add a custom item


        NbtMapBuilder builder = NbtMap.builder();
        builder.putString("name", "geysermc:" + itemType + textureData + customModelData)
                .putInt("id", itemId);

        NbtMapBuilder itemProperties = NbtMap.builder();

        NbtMapBuilder componentBuilder = NbtMap.builder();
        // Conveniently, as of 1.16.200, the furnace minecart has a textureData AND translation string already.
        // 1.17.30 moves the icon to the item properties section
        itemProperties.putCompound("minecraft:icon", NbtMap.builder()
                .putString("textureData", textureData).build());
        componentBuilder.putCompound("minecraft:display_name", NbtMap.builder().putString("value", "geysermc.item.custom." + itemId).build());

        // We always want to allow offhand usage when we can - matches Java Edition
        itemProperties.putBoolean("allow_off_hand", true);

        componentBuilder.putCompound("item_properties", itemProperties.build());
        builder.putCompound("components", componentBuilder.build());
        customItemData = new ComponentItemData("geysermc:" + itemType + textureData + customModelData, builder.build());
        return customItemData;
    }



    public List<StartGamePacket.ItemEntry> getAllItems(GeyserSession session) {
        List<StartGamePacket.ItemEntry> entries = session.getItemMappings().getItemEntries();
        entries.addAll(bedrockCustomItems);
        return entries;
    }
}
