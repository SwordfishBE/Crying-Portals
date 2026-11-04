Add-Type -AssemblyName System.Drawing
$bitmap = [System.Drawing.Bitmap]::new(128, 128)
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
function Rect($color, $x, $y, $w, $h) {
    $brush = [System.Drawing.SolidBrush]::new([System.Drawing.ColorTranslator]::FromHtml($color))
    $graphics.FillRectangle($brush, $x, $y, $w, $h)
    $brush.Dispose()
}
Rect '#151020' 0 0 128 128
Rect '#a447dc' 40 24 48 80
Rect '#7930b6' 48 32 32 64
Rect '#ba65ed' 56 40 16 48
for ($row = 0; $row -lt 6; $row++) {
    for ($column = 0; $column -lt 4; $column++) {
        if ($column -eq 0 -or $column -eq 3 -or $row -eq 0 -or $row -eq 5) {
            $x = 24 + $column * 20
            $y = 4 + $row * 20
            Rect '#30243f' $x $y 20 20
            Rect '#4a355d' ($x + 2) ($y + 2) 16 4
            Rect '#9d56d1' ($x + 8) ($y + 7) 4 7
            Rect '#d18aff' ($x + 12) ($y + 13) 4 4
        }
    }
}
Rect '#dfb3ff' 18 44 4 8
Rect '#b877e8' 106 76 4 12
$bitmap.Save((Join-Path $PSScriptRoot '../src/main/resources/assets/crying_portals/icon.png'), [System.Drawing.Imaging.ImageFormat]::Png)
$graphics.Dispose()
$bitmap.Dispose()
