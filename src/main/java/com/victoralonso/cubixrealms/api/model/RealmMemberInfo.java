package com.victoralonso.cubixrealms.api.model;

import java.util.UUID;

/**
 * Immutable snapshot of a realm member's role.
 *
 * @param memberUuid UUID of the member
 * @param roleName   role name (e.g. TRUSTED, RESIDENT, OWNER, VISITOR)
 * @param joinedAt   epoch-millis timestamp of when the member joined
 */
public record RealmMemberInfo(
        UUID memberUuid,
        String roleName,
        long joinedAt
) {}
