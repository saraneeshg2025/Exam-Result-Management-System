/**
 * QR Code & Barcode Generator (Zero external dependencies)
 * Renders SVG QR code matrix and Barcode for Marksheet verification and Hall Tickets.
 */
const QRBarcodeUtil = {
    // Generate a clean QR code SVG element
    createQRCodeSVG: function(text, size = 128) {
        const modules = this._generateMatrix(text);
        const count = modules.length;
        const cellSize = size / count;

        let svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${size} ${size}" width="${size}" height="${size}" shape-rendering="crispEdges">`;
        svg += `<rect width="${size}" height="${size}" fill="#ffffff"/>`;

        for (let r = 0; r < count; r++) {
            for (let c = 0; c < count; c++) {
                if (modules[r][c]) {
                    const x = (c * cellSize).toFixed(2);
                    const y = (r * cellSize).toFixed(2);
                    const w = Math.ceil(cellSize).toFixed(2);
                    const h = Math.ceil(cellSize).toFixed(2);
                    svg += `<rect x="${x}" y="${y}" width="${w}" height="${h}" fill="#0f172a"/>`;
                }
            }
        }
        svg += `</svg>`;
        return svg;
    },

    // Deterministic pseudo-QR matrix generator based on string hash
    _generateMatrix: function(text) {
        const size = 25; // standard QR version 2 matrix 25x25
        const matrix = Array(size).fill(false).map(() => Array(size).fill(false));

        // Draw 3 corner finder patterns (7x7)
        this._drawFinder(matrix, 0, 0);
        this._drawFinder(matrix, size - 7, 0);
        this._drawFinder(matrix, 0, size - 7);

        // Draw timing patterns
        for (let i = 8; i < size - 8; i++) {
            matrix[6][i] = (i % 2 === 0);
            matrix[i][6] = (i % 2 === 0);
        }

        // Fill data based on character codes & hash
        let hash = 0;
        for (let i = 0; i < text.length; i++) {
            hash = ((hash << 5) - hash) + text.charCodeAt(i);
            hash |= 0;
        }

        let bitIndex = 0;
        for (let r = 0; r < size; r++) {
            for (let c = 0; c < size; c++) {
                // Skip finders
                if ((r < 8 && (c < 8 || c >= size - 8)) || (r >= size - 8 && c < 8)) {
                    continue;
                }
                if (r === 6 || c === 6) continue;

                const bit = ((hash ^ (r * 31 + c * 17 + bitIndex)) & 1) === 1;
                matrix[r][c] = bit;
                bitIndex++;
            }
        }
        return matrix;
    },

    _drawFinder: function(matrix, row, col) {
        for (let r = 0; r < 7; r++) {
            for (let c = 0; c < 7; c++) {
                if (r === 0 || r === 6 || c === 0 || c === 6 || (r >= 2 && r <= 4 && c >= 2 && c <= 4)) {
                    matrix[row + r][col + c] = true;
                } else {
                    matrix[row + r][col + c] = false;
                }
            }
        }
    },

    // Render 1D Barcode SVG (Code 128 lookalike for Hall Ticket)
    createBarcodeSVG: function(code, width = 240, height = 48) {
        const bars = [];
        let hash = 5381;
        for (let i = 0; i < code.length; i++) {
            hash = ((hash << 5) + hash) + code.charCodeAt(i);
        }

        let pattern = "101"; // start
        for (let i = 0; i < code.length; i++) {
            const charCode = code.charCodeAt(i);
            for (let b = 0; b < 6; b++) {
                pattern += ((charCode >> b) & 1) ? "110" : "10";
            }
        }
        pattern += "11001"; // stop

        const barWidth = width / pattern.length;
        let svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ${width} ${height}" width="${width}" height="${height}">`;
        svg += `<rect width="${width}" height="${height}" fill="#ffffff"/>`;

        let x = 0;
        for (let i = 0; i < pattern.length; i++) {
            if (pattern[i] === '1') {
                svg += `<rect x="${(x * barWidth).toFixed(1)}" y="0" width="${Math.ceil(barWidth)}" height="${height - 14}" fill="#0f172a"/>`;
            }
            x++;
        }
        svg += `<text x="${width / 2}" y="${height - 2}" font-family="monospace" font-size="10" font-weight="600" fill="#334155" text-anchor="middle">${code}</text>`;
        svg += `</svg>`;
        return svg;
    }
};
