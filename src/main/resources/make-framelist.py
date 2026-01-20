import os
import time

snippet = r"""# Here list the filenames of the .png frames you put inside the 'frames' folder
# (full names, like frame1.png)
# (the order DOES matter)

# You can automate this by running the python script supplied in the root directory

{}
"""

framesPath = "./assets/deathscreenframes/frames"

frames = os.listdir(framesPath)
framesRen = []

for i, fn in enumerate(frames):
    fn2 = "res%d_%d.png" % (
        i,
        time.time()
    )
    os.rename(framesPath + "/" + fn, framesPath + "/" + fn2)
    framesRen.append(fn2)

with open("./assets/deathscreenframes/framelist.txt", "w", encoding="utf-8") as f:
    f.write(snippet.format("\n".join(framesRen)))
