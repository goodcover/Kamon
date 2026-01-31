{
  inputs = {
    gc-nix.url = "github:goodcover/gc-nix";
    flake-utils.follows = "gc-nix/flake-utils";
    nixpkgs.follows = "gc-nix/nixpkgs";
  };

  outputs = { self, gc-nix, flake-utils, nixpkgs }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
        };

        shell = gc-nix.devShells.${system}.jdk17;

        sbt = pkgs.sbt.override {  jre = shell.jdk; };

        inputs = [
          shell.jdk
          sbt
        ];
      in
      {

        devShell = pkgs.mkShell {
          buildInputs = inputs;

          SBT_OPTS = ''-Xms5120M -Xmx5120M -Xss6M
            '';
        };

        devShells = gc-nix.devShells;

        pkgs = pkgs;
      }
    );
}
