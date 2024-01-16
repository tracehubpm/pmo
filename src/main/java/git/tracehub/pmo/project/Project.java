/*
 * Copyright (c) 2023-2024 Tracehub
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

package git.tracehub.pmo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project.
 *
 * @since 0.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    /**
     * Id.
     */
    private UUID id;

    /**
     * Name.
     */
    @NotBlank(message = "Name can't be blank")
    private String name;

    /**
     * Business Model.
     */
    private String model;

    /**
     * Description.
     */
    private String description;

    /**
     * Status.
     */
    @NotNull(message = "Status can't be blank")
    private Status status;

    /**
     * Location.
     */
    @NotBlank(message = "Location can't be blank")
    private String location;

    /**
     * Start date.
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String start;

    /**
     * End date.
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String end;

    /**
     * Visibility.
     */
    @NotNull(message = "Visibility can't be blank")
    private boolean secured;

    /**
     * Subscribers.
     */
    private String subscribers;

    /**
     * Status.
     */
    public enum Status {

        /**
         * Untracked.
         */
        UNTRACKED,

        /**
         * Tracking.
         */
        TRACKING,
    }

}
