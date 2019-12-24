/*
 * Copyright © 2019 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.option;

import me.lambdaurora.spruceui.SpruceButtonWidget;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Represents a boolean option.
 * <p>
 * Works the same as the vanilla one but can provide a tooltip.
 */
public class SpruceBooleanOption extends Option
{
    private final Predicate<GameOptions>           getter;
    private final BiConsumer<GameOptions, Boolean> setter;
    private final Text                             tooltip;

    public SpruceBooleanOption(@NotNull String key, @NotNull Predicate<GameOptions> getter, @NotNull BiConsumer<GameOptions, Boolean> setter, @Nullable Text tooltip)
    {
        super(key);
        this.getter = getter;
        this.setter = setter;
        this.tooltip = tooltip;
    }

    public void set(@NotNull GameOptions options, @NotNull String value)
    {
        this.set(options, "true".equals(value));
    }

    public void set(@NotNull GameOptions options)
    {
        this.set(options, !this.get(options));
        options.write();
    }

    private void set(@NotNull GameOptions options, boolean value)
    {
        this.setter.accept(options, value);
    }

    public boolean get(@NotNull GameOptions options)
    {
        return this.getter.test(options);
    }

    @Override
    public @NotNull AbstractButtonWidget createButton(@NotNull GameOptions options, int x, int y, int width)
    {
        SpruceButtonWidget button = new SpruceButtonWidget(x, y, width, 20, this.get_display_string(options), btn -> {
            this.set(options);
            btn.setMessage(this.get_display_string(options));
        });
        button.set_tooltip(this.tooltip);
        return button;
    }

    public @NotNull String get_display_string(@NotNull GameOptions options)
    {
        return this.getDisplayPrefix() + I18n.translate(this.get(options) ? "options.on" : "options.off");
    }
}
