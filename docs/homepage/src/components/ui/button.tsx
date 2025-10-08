import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";

import { cn } from "./utils";

const buttonVariants = cva(
    "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-xl text-sm font-semibold transition-all duration-200 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg:not([class*='size-'])]:size-4 shrink-0 [&_svg]:shrink-0 outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transform hover:scale-[1.02] active:scale-[0.98]",
    {
        variants: {
            variant: {
                default:
                    "bg-gradient-to-r from-accent via-accent to-accent/90 text-accent-foreground hover:from-accent/95 hover:via-accent/85 hover:to-accent/75 shadow-lg shadow-accent/30 hover:shadow-xl hover:shadow-accent/40 border border-accent/30 hover:border-accent/40 backdrop-blur-sm",
                destructive:
                    "bg-destructive text-white hover:bg-destructive/90 focus-visible:ring-destructive/20 dark:focus-visible:ring-destructive/40 dark:bg-destructive/60 shadow-lg shadow-destructive/25",
                outline:
                    "border-2 border-accent/60 bg-background/50 backdrop-blur-sm text-foreground hover:bg-accent/10 hover:border-accent shadow-sm hover:shadow-md",
                secondary:
                    "bg-secondary text-secondary-foreground hover:bg-secondary/80 shadow-sm hover:shadow-md",
                ghost: "hover:bg-accent/10 hover:text-accent-foreground dark:hover:bg-accent/10 rounded-lg",
                link: "text-primary underline-offset-4 hover:underline",
            },
            size: {
                default: "h-10 px-5 py-2.5 has-[>svg]:px-4",
                sm: "h-9 rounded-lg gap-1.5 px-4 has-[>svg]:px-3",
                lg: "h-12 rounded-xl px-8 text-base has-[>svg]:px-6",
                icon: "size-10 rounded-xl",
            },
        },
        defaultVariants: {
            variant: "default",
            size: "default",
        },
    }
);

function Button({
    className,
    variant,
    size,
    asChild = false,
    ...props
}: React.ComponentProps<"button"> &
    VariantProps<typeof buttonVariants> & {
        asChild?: boolean;
    }) {
    const Comp = "button";

    return (
        <Comp
            data-slot="button"
            className={cn(buttonVariants({ variant, size, className }))}
            {...props}
        />
    );
}

export { Button };
