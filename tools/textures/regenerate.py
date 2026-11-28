#!/usr/bin/env python3
"""Regenerate TFIE drill textures from IE shapes and TFC/TFCM ingot palettes.

Requires Python 3.10+ and Pillow. By default, generated files are staged under
``.tmp/drill-textures``. Use ``--check`` to compare them with the resource
textures, or ``--apply`` to install them.
"""

import argparse
from pathlib import Path
import shutil

from PIL import Image


HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[1]
REFERENCE = HERE / "reference"
ASSETS = ROOT / "src/main/resources/assets/tfie/textures/item"
METALS = (
    "black_steel",
    "blue_steel",
    "red_steel",
    "invar",
    "titanium",
    "netherite",
    "tungsten_steel",
)


def pixels(image: Image.Image):
    if hasattr(image, "get_flattened_data"):
        return list(image.get_flattened_data())
    return list(image.getdata())


def palette(image: Image.Image):
    return sorted(
        {pixel[:3] for pixel in pixels(image.convert("RGBA")) if pixel[3]},
        key=lambda color: (0.2126 * color[0] + 0.7152 * color[1] + 0.0722 * color[2], color),
    )


def sample(colors, position: float):
    scaled = position * (len(colors) - 1)
    index = min(int(scaled), max(0, len(colors) - 2))
    fraction = scaled - index
    following = min(index + 1, len(colors) - 1)
    return tuple(round(colors[index][channel] * (1 - fraction)
                       + colors[following][channel] * fraction) for channel in range(3))


def generate(kind: str, metal: str) -> Image.Image:
    shape = Image.open(REFERENCE / "immersiveengineering" / f"{kind}_iron.png").convert("RGBA")
    colors = palette(Image.open(REFERENCE / "ingot" / f"{metal}.png"))
    shades = palette(shape)
    replacements = {
        shade: sample(colors, index / (len(shades) - 1) if len(shades) > 1 else 0.5)
        for index, shade in enumerate(shades)
    }
    output = Image.new("RGBA", shape.size)
    output.putdata([
        (*replacements[pixel[:3]], pixel[3]) if pixel[3] else (0, 0, 0, 0)
        for pixel in pixels(shape)
    ])
    return output


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    modes = parser.add_mutually_exclusive_group()
    modes.add_argument("--apply", action="store_true")
    modes.add_argument("--check", action="store_true")
    parser.add_argument("--output", type=Path, default=ROOT / ".tmp/drill-textures")
    args = parser.parse_args()

    differences = []
    for kind in ("drill", "drillhead"):
        for metal in METALS:
            result = generate(kind, metal)
            name = f"{kind}_{metal}.png"
            staged = args.output / name
            staged.parent.mkdir(parents=True, exist_ok=True)
            result.save(staged, optimize=True)

            current = Image.open(ASSETS / name).convert("RGBA")
            if current.size != result.size or current.tobytes() != result.tobytes():
                differences.append(name)
            if args.apply:
                shutil.copyfile(staged, ASSETS / name)

    if args.check and differences:
        raise SystemExit(f"{len(differences)} textures differ: {differences}")
    state = "applied" if args.apply else "checked" if args.check else "staged"
    print(f"{len(METALS) * 2} textures; {len(differences)} pixel changes; {state}.")


if __name__ == "__main__":
    main()
