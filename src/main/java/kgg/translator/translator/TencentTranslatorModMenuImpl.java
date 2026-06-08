package kgg.translator.translator;

import kgg.translator.modmenu.ModMenuConfigurable;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.network.chat.Component;

public class TencentTranslatorModMenuImpl extends TencentTranslatorImpl implements ModMenuConfigurable {
    @Override
    public Runnable registerEntry(ConfigEntryBuilder entryBuilder, SubCategoryBuilder category) {
        StringListEntry secretIdEntry = entryBuilder.startStrField(Component.literal("SecretId"), secretId).build();
        StringListEntry secretKeyEntry = entryBuilder.startStrField(Component.literal("SecretKey"), secretKey).build();
        StringListEntry regionEntry = entryBuilder.startStrField(Component.literal("Region"), region).build();
        IntegerListEntry projectIdEntry = entryBuilder.startIntField(Component.literal("ProjectId"), projectId).build();
        IntegerListEntry qpsEntry = entryBuilder.startIntField(Component.literal("QPS"), 1000 / delayTime).build();

        category.add(secretIdEntry);
        category.add(secretKeyEntry);
        category.add(regionEntry);
        category.add(projectIdEntry);
        category.add(qpsEntry);

        return () -> {
            setConfig(
                    secretIdEntry.getValue(),
                    secretKeyEntry.getValue(),
                    regionEntry.getValue(),
                    projectIdEntry.getValue()
            );
            setDelayTime(1000 / qpsEntry.getValue());
        };
    }
}