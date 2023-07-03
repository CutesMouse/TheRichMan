package com.cutesmouse.therichman.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BuildTools {
    public static void pasteSchematic(File file, Location loc, int angle) {
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            Clipboard clipboard = format.getReader(new FileInputStream(file)).read();
            EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()));
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            holder.setTransform(new AffineTransform().rotateY(-angle));
            Operation operation = holder.createPaste(session).to(BlockVector3
                    .at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())).build();
            Operations.complete(operation);
            session.flushSession();
        } catch (IOException | WorldEditException e) {
            throw new RuntimeException(e);
        }
    }
}
