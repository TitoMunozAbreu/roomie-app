package es.roomie.household.kafka;

/**
 * Represents an invitation for a new member to join a household.
 * This record holds the details of the invitation including
 * title, description, member's email, and a confirmation URL.
 *
 * @param title The title of the invitation.
 * @param description A brief description of the invitation.
 * @param memberEmail The email address of the member being invited.
 * @param urlConfirmInvitation The URL for the member to confirm their invitation.
 */
public record NewMemberInvitation(
        String title,
        String description,
        String memberEmail,
        String urlConfirmInvitation
) {}
