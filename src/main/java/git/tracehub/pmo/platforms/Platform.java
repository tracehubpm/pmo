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

package git.tracehub.pmo.platforms;

import org.cactoos.Scalar;

/**
 * Platform.
 *
 * @since 0.0.0
 */
public interface Platform {

    /**
     * Create webhook.
     *
     * @param token Token
     * @param location Repository location
     */
    void createWebhook(Scalar<String> token, Scalar<String> location);

    /**
     * Create label.
     *
     * @param token Token
     * @param location Repository location
     */
    void createLabel(Scalar<String> token, Scalar<String> location);

    /**
     * Invite collaborator.
     *
     * @param token Token
     * @param location Repository location
     */
    void inviteCollaborator(Scalar<String> token, Scalar<String> location);

}
