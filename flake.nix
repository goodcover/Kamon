{
  inputs = {
    gc-nix.url = "github:goodcover/gc-nix?ref=main";
    flake-utils.follows = "gc-nix/flake-utils";
    nixpkgs.follows = "gc-nix/nixpkgs";
  };

  outputs = { self, gc-nix, flake-utils, nixpkgs }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
        };

        sbt = pkgs.sbt.override {  jre = pkgs.jdk8; };

        inputs = [
          pkgs.jdk8
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
