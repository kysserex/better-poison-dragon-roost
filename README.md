# Poison Dragons: Swamp Nests (addon, Forge 1.20.1)

## Que hace
- **Nidos (roost) y cuevas (cave)**: despues de generarse, los bloques propios de Poison Dragons se cambian por:
  - `poisoned_stone`        -> barro, tierra con raices, raices de manglar (y enlodadas)
  - `poisoned_cobblestone`  -> troncos, madera y tablas de roble oscuro, con barro y raices
  - `poisoned_pile`         -> grava sospechosa (con loot propio) o grava normal
  En las cuevas tambien cambian las estalactitas (de piedra envenenada) y se pueden desactivar por separado
  con `convertRoosts` y `convertCaves` en la config.
- **Aliento**: el chorro y la bola de veneno dejan una nube de veneno de unos segundos.
  El dragon, su jinete, su dueno y otros dragones de veneno no sufren la nube.
- Todo se ajusta en `config/pd_swamp_nests-common.toml`.

## Compilar sin instalar nada (GitHub Actions)
1. Crea un repositorio en GitHub y sube TODO el contenido de esta carpeta (incluida `.github`).
2. Pestana **Actions** -> workflow **Build jar** -> espera a que termine (unos 5 min).
3. Entra en la ejecucion y descarga el artefacto `pd_swamp_nests-jar`: dentro esta el .jar listo para `mods/`.

## Compilar en tu PC
1. (Opcional si ya tienes Gradle 8.1.1) Copia a esta carpeta `gradlew`, `gradlew.bat` y la carpeta `gradle/` del MDK oficial de Forge 1.20.1
   (https://files.minecraftforge.net, version 47.x). El wrapper no se incluye porque no se puede descargar aqui.
2. Necesitas JDK 17.
3. Ejecuta `./gradlew build` (Windows: `gradlew.bat build`).
4. El jar queda en `build/libs/pd_swamp_nests-1.0.0.jar`.

## Instalar
Pon el jar en `mods/` junto a Poison Dragons 0.5 y Citadel. Los nidos nuevos solo aparecen en chunks
que aun no se hayan generado (crea un mundo nuevo o explora zonas nuevas).

## Archivos clave
- `mixin/RoostPieceMixin.java`   engancha el final de la generacion del nido
- `mixin/CavePieceMixin.java`    lo mismo para la cueva
- `NestConverter.java`           tabla de bloques de reemplazo (aqui cambias que bloque va donde)
- `mixin/DragonBreathMixin.java` nube del chorro
- `ForgeEvents.java`             nube de la bola + proteccion del dragon/jinete/dueno
- `data/.../archaeology/roost.json` loot de la grava sospechosa
