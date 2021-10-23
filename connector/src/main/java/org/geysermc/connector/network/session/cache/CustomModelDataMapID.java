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

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.nbt.NbtType;
import com.nukkitx.protocol.bedrock.data.inventory.ComponentItemData;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.geysermc.connector.network.session.GeyserSession;

import java.util.*;

public class CustomModelDataMapID {
    private final Map<Integer, String> mapping;
    private final List<StartGamePacket.ItemEntry> items = new ObjectArrayList<>();
    private final List<ComponentItemData> componentData = new ObjectArrayList<>();


    public List<ComponentItemData> getComponentData() {
        return componentData;
    }

    public Map<Integer, String> getMappings() {
        return mapping;
    }

    public List<StartGamePacket.ItemEntry> getItems() {
        return items;
    }public List<StartGamePacket.ItemEntry> getAllItems(GeyserSession session) {
       List<StartGamePacket.ItemEntry> items = new ArrayList<>(session.getItemMappings().getItemEntries());
       items.addAll(items);
       return items;
   }


    public CustomModelDataMapID(GeyserSession session){
        List<String> list = session.getConnector().getConfig().getCustomModelDataMapID();
        mapping = new HashMap<>();
        for (String s : list) {
            mapping.put(Integer.parseInt(s.split(";")[0]), s.split(";")[1]);
        }
        int index = session.getItemMappings().getItems().size();
        for (int key : mapping.keySet()) {
            index++;
            NbtMapBuilder builder = NbtMap.builder();
            builder.putString("name", mapping.get(key))
                    .putInt("id", session.getItemMappings().getItems().size() + 1);

            NbtMapBuilder itemProperties = NbtMap.builder();

            NbtMapBuilder componentBuilder = NbtMap.builder();
            // Conveniently, as of 1.16.200, the furnace minecart has a texture AND translation string already.
            // 1.17.30 moves the icon to the item properties section

            // Indicate that the arm animation should play on rails
            List<NbtMap> useOnTag = Collections.singletonList(NbtMap.builder().putString("tags", "q.any_tag('rail')").build());

            itemProperties.putBoolean("allow_off_hand", true);
            itemProperties.putBoolean("hand_equipped", true);
            itemProperties.putInt("max_stack_size", 64);

            componentBuilder.putCompound("item_properties", itemProperties.build());
            builder.putCompound("components", componentBuilder.build());
            componentData.add(new ComponentItemData(mapping.get(key), builder.build()));
            items.add(new StartGamePacket.ItemEntry(mapping.get(key), (short) (session.getItemMappings().getItems().size() + 1)));
        }
        System.out.println(items);
        System.out.println(componentData);
    }
}
