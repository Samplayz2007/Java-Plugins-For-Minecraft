/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2022 Arcane Arts (Volmit Software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.volmit.iris.util.json;


/**
 * The HTTPTokener extends the JSONTokener to provide additional methods for the
 * parsing of HTTP headers.
 *
 * @author JSON.org
 * @version 2014-05-03
 */
public class HTTPTokener extends JSONTokener {

    /**
     * Construct an HTTPTokener from a string.
     *
     * @param string A source string.
     */
    public HTTPTokener(String string) {
        super(string);
    }

    /**
     * Get the next token or string. This is used in parsing HTTP headers.
     *
     * @return A String.
     */
    public String nextToken() throws JSONException {
        char c;
        char q;
        StringBuilder sb = new StringBuilder();
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == '"' || c == '\'') {
            q = c;
            for (; ; ) {
                c = next();
                if (c < ' ') {
                    throw syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return sb.toString();
                }
                sb.append(c);
            }
        }
        for (; ; ) {
            if (c == 0 || Character.isWhitespace(c)) {
                return sb.toString();
            }
            sb.append(c);
            c = next();
        }
    }
}
