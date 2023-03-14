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

    public Value getValue(UUID owner) {
        return this.data.getValue(owner);
    }

    public void setValue(UUID owner, Value value) {
        this.data.setValue(owner, value);
    }


    public static class SpellStorageData extends SavedData {
        private final Map<UUID, Value> storage;

        public SpellStorageData() {
            this.storage = new HashMap<>();
        }

        public Value getValue(UUID owner) {
            return this.storage.get(owner);
        }

        public void setValue(UUID owner, Value value) {
            this.storage.put(owner, value);
            this.setDirty();
        }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
            CompoundTag storageTag = new CompoundTag();
            storage.forEach((ownerUuid, playerValue) -> {
                if (playerValue != null) {
                    storageTag.put(ownerUuid.toString(), playerValue.toNbt());
                }
            });

            nbt.put("SpellStorage", storageTag);
            return nbt;
        }

        public void load(CompoundTag nbt) {
            CompoundTag storageTag = nbt.getCompound("SpellStorage");
            for (String uuid : storageTag.getAllKeys()) {
                CompoundTag playerValue = nbt.getCompound(uuid);
                this.storage.put(UUID.fromString(uuid), Value.fromNbt(playerValue));
            }
        }

        public static SpellStorageData createFromNbt(CompoundTag nbt) {
            SpellStorageData data = new SpellStorageData();
            data.load(nbt);
            return data;
        }
    }
}
