CREATE TABLE workspaces (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    template VARCHAR(20) NOT NULL
);

CREATE TABLE workspace_roles (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces (id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_workspace_role_name UNIQUE (workspace_id, name)
);

CREATE TABLE workspace_channels (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces (id) ON DELETE CASCADE,
    name VARCHAR(80) NOT NULL,
    kind VARCHAR(20) NOT NULL,
    CONSTRAINT uq_workspace_channel_name UNIQUE (workspace_id, name)
);

CREATE TABLE workspace_memberships (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces (id) ON DELETE CASCADE,
    user_subject VARCHAR(255) NOT NULL,
    role_id UUID NOT NULL REFERENCES workspace_roles (id),
    CONSTRAINT uq_workspace_member UNIQUE (workspace_id, user_subject)
);
