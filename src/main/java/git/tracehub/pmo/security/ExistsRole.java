/*
 * Copyright (c) 2023-2024 Tracehub.git
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package git.tracehub.pmo.security;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Does the user have provided role from token.
 *
 * @since 0.0.0
 */
@RequiredArgsConstructor
public final class ExistsRole implements Scalar<Boolean> {

    /**
     * JWT.
     */
    private final Jwt jwt;

    /**
     * Role.
     */
    private final String role;

    @Override
    public Boolean value() {
        final Map<String, Object> map = this.jwt.getClaimAsMap("realm_access");
        return ((List<String>) map.get("roles")).contains(this.role);
    }

}
