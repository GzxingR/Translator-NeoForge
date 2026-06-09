package kgg.translator;

import kgg.translator.command.LLMConfigCommand;
import kgg.translator.command.TranslateCommand;
import kgg.translator.command.TranslateConfigCommand;
import kgg.translator.handler.KeyBindingHandler;
import kgg.translator.option.OptionRegistry;
import kgg.translator.option.Options;
import kgg.translator.platform.PlatformHooks;
import kgg.translator.translator.BaiduTranslatorModMenuImpl;
import kgg.translator.translator.LLMTranslatorImpl;
import kgg.translator.translator.TencentTranslatorModMenuImpl;
import kgg.translator.translator.YouDaoTranslatorModMenuImpl;
import kgg.translator.translator.BingTranslator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;

import java.io.File;

@Mod(TranslatorMod.MOD_ID)
public class TranslatorMod {
    public static final String MOD_ID = "translator";

    public TranslatorMod(IEventBus modEventBus, ModContainer modContainer) {
        PlatformHooks.init(() -> "1.21.1", () -> FMLPaths.CONFIGDIR.get());
        LLMManager.setTranslatorFactory(LLMTranslatorImpl::new);
        TranslatorConfig.setOptionStorage(OptionRegistry.INSTANCE);

        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(KeyBindingHandler::registerKeys);
        NeoForge.EVENT_BUS.addListener(KeyBindingHandler::onClientTick);
        NeoForge.EVENT_BUS.addListener(TranslateCommand::register);
        NeoForge.EVENT_BUS.addListener(TranslateConfigCommand::register);
        NeoForge.EVENT_BUS.addListener(LLMConfigCommand::register);

        new File("config/translator").mkdirs();
        Options.init();
        TranslatorManager.addTranslator(new BaiduTranslatorModMenuImpl());
        TranslatorManager.addTranslator(new YouDaoTranslatorModMenuImpl());
        TranslatorManager.addTranslator(new TencentTranslatorModMenuImpl());
        TranslatorManager.addTranslator(new BingTranslator());

        Runtime.getRuntime().addShutdownHook(new Thread(TranslatorConfig::writeFile));
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(TranslatorConfig::readFile);
    }
}
