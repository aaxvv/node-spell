package eu.aaxvv.node_spell.spell.execution;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SpellPersistentStorage {
    private SpellStorageData data;
    public void init(MinecraftServer server) {
        ServerLevel overworld = Objects.requireNonNull(server.getLevel(Level.OVERWORLD));
        DimensionDataStorage storage = overworld.getDataStorage();

        this.data = storage.computeIfAbsent(SpellStorageData::createFromNbt, SpellStorageData::new, ModConstants.MOD_ID);
    }

    public Value getValue(UUID owner, String spellName) {
        return this.data.getValue(owner, spellName);
    }

    public void setValue(UUID owner, String spellName, Value value) {
        this.data.setValue(owner, spellName, value);
    }

    public void removeOwner(UUID owner) {
        this.data.removeOwner(owner);
    }
    public boolean ownerExists(UUID owner) {
        return this.data.ownerExists(owner);
    }


    public static class SpellStorageData extends SavedData {
        private final Map<UUID, Map<String, Value>> storage;

        public SpellStorageData() {
            this.storage = new HashMap<>();
        }

        public Value getValue(UUID owner, String spellName) {
            Map<String, Value> playerStorage = this.storage.computeIfAbsent(owner, key -> new HashMap<>());
            return playerStorage.get(spellName);
        }

        public void setValue(UUID owner, String spellName, Value value) {
            Map<String, Value> playerStorage = this.storage.computeIfAbsent(owner, key -> new HashMap<>());
            playerStorage.put(spellName, value);
            this.setDirty();
        }

        public void removeOwner(UUID owner) {
            this.storage.remove(owner);
            this.setDirty();
        }

        public boolean ownerExists(UUID owner) {
            return this.storage.containsKey(owner);
        }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
            storage.forEach((ownerUuid, playerStorage) -> {
                CompoundTag ownerTag = new CompoundTag();
                playerStorage.forEach((spellName, value) -> {
                    ownerTag.put(spellName, value.toNbt());
                });
                nbt.put(ownerUuid.toString(), ownerTag);
            });

            return nbt;
        }

        public void load(CompoundTag nbt) {
            for (String uuid : nbt.getAllKeys()) {
                Map<String, Value> valueMap = new HashMap<>();
                CompoundTag ownerValuesNbt = nbt.getCompound(uuid);
                for (String spellName : ownerValuesNbt.getAllKeys()) {
                    valueMap.put(spellName, Value.fromNbt(ownerValuesNbt.getCompound(spellName)));
                }
                this.storage.put(UUID.fromString(uuid), valueMap);
            }
        }

        public static SpellStorageData createFromNbt(CompoundTag nbt) {
            SpellStorageData data = new SpellStorageData();
            data.load(nbt);
            return data;
        }
    }
}
