package dev.compactmods.machines.room.ui.preview;

import dev.compactmods.machines.api.CompactMachines;
import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.room.Rooms;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MachineRoomMenu extends AbstractContainerMenu {
    private final String room;
    private String roomName;
    private final GlobalPos machine;
    private StructureTemplate roomBlocks;
    public boolean loadingBlocks;

    public MachineRoomMenu(int win, String room, GlobalPos machine, String roomName) {
        super(Rooms.Menus.MACHINE_MENU.get(), win);
        this.room = room;
        this.roomName = roomName;
        this.roomBlocks = new StructureTemplate();
        this.machine = machine;
        this.loadingBlocks = false;
    }

    public String getRoom() {
        return room;
    }

    public GlobalPos getMachine() {
        return machine;
    }

    public static MenuProvider provider(MinecraftServer server, RoomInstance roomInstance) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable(CompactMachines.MOD_ID + ".ui.room");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int winId, Inventory inv, Player player) {
                var viewing = player.getData(Rooms.DataAttachments.OPEN_MACHINE_POS);
                return new MachineRoomMenu(winId, roomInstance.code(), viewing, "Room Preview");

            }

            @Override
            public boolean shouldTriggerClientSideContainerClosingOnOpen() {
                return false;
            }
        };
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int slotInd) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Nullable
    public StructureTemplate getBlocks() {
        return roomBlocks;
    }

    public void setBlocks(StructureTemplate blocks) {
        this.roomBlocks = blocks;
        this.loadingBlocks = false;
    }

    public String getRoomName() {
        return roomName;
    }

    public static MachineRoomMenu createClientMenu(int windowId, Inventory inv, FriendlyByteBuf data) {
        final var mach = data.readJsonWithCodec(GlobalPos.CODEC);
        final var room = data.readUtf();
        final var roomName = data.readOptional(FriendlyByteBuf::readUtf).orElse("Room Preview");

        return new MachineRoomMenu(windowId, room, mach, roomName);
    }
}
