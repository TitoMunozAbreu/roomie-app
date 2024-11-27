package es.roomie.notification.kafka.household;

/**
 * Represents an invitation for a new member to join a household.
 * This record contains the essential details for the invitation.
 *
 * @param title               The title of the invitation.
 * @param description         A description of the invitation.
 * @param memberEmail         The email address of the new member being invited.
 * @param urlConfirmInvitation The URL for the new member to confirm their invitation.
 */
public record NewMemberInvitation(
        String title,
        String description,
        String memberEmail,
        String urlConfirmInvitation
) {}
